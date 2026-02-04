package fun.gusmurphy.library.springboothex.domain;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.OverdueNotificationSpy;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.domain.port.driving.ChecksForOverdueBooks;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OverdueNotificationServiceTest {

    private final TestClock testClock = new TestClock();
    private final BookRepositoryDouble bookRepository = new BookRepositoryDouble();
    private final OverdueNotificationSpy notificationSpy = new OverdueNotificationSpy();
    private static final Isbn ISBN = Isbn.fromString("my-isbn");
    private static final int BOOK_CHECKOUT_TIME_IN_DAYS = 7;
    private static final ZonedDateTime TEST_TIME = ZonedDateTime.now();

    private final ChecksForOverdueBooks service =
            new OverdueNotificationService(bookRepository, testClock, notificationSpy);

    @BeforeEach
    void setup() {
        bookRepository.clear();
        var book = new Book(ISBN, BOOK_CHECKOUT_TIME_IN_DAYS);
        bookRepository.saveBook(book);
        testClock.setCurrentTime(TEST_TIME);
        notificationSpy.reset();
    }

    @Test
    void noNotificationsAreSentIfNothingIsOverdue() {
        // Checking out a book at test time (meaning it won't be overdue for a while)...
        var book = bookRepository.findByIsbn(ISBN).get();
        book.checkout(UserId.random(), TEST_TIME);
        bookRepository.saveBook(book);

        service.checkForOverdueBooks();
        assertTrue(notificationSpy.noNotificationsSent());
    }

    @ParameterizedTest
    @MethodSource("checkoutTimesThatShouldBeOverdueByTestTime")
    void aNotificationIsSentForASingleOverdueBook(ZonedDateTime checkoutTime) {
        var book = bookRepository.findByIsbn(ISBN).get();
        var userId = UserId.random();
        book.checkout(userId, checkoutTime);
        bookRepository.saveBook(book);

        service.checkForOverdueBooks();

        var notification = notificationSpy.latestNotification();

        assertEquals(ISBN, notification.isbn());
        assertEquals(userId, notification.userId());

        var expectedLateTime = checkoutTime.plusDays(BOOK_CHECKOUT_TIME_IN_DAYS);
        assertEquals(expectedLateTime, notification.lateAsOf());
    }

    private static Stream<Arguments> checkoutTimesThatShouldBeOverdueByTestTime() {
        return Stream.of(
                Arguments.of(TEST_TIME.minusDays(BOOK_CHECKOUT_TIME_IN_DAYS)),
                Arguments.of(TEST_TIME.minusDays(BOOK_CHECKOUT_TIME_IN_DAYS + 1)));
    }

    @Test
    void notificationsAreSentForMultipleOverdueBooks() {
        var bookA = new Book(Isbn.fromString("isbn-a"), BOOK_CHECKOUT_TIME_IN_DAYS);
        var bookB = new Book(Isbn.fromString("isbn-b"), BOOK_CHECKOUT_TIME_IN_DAYS);

        var userIdA = UserId.random();
        var userIdB = UserId.random();

        var checkoutTime = TEST_TIME.minusDays(BOOK_CHECKOUT_TIME_IN_DAYS);
        bookA.checkout(userIdA, checkoutTime);
        bookB.checkout(userIdB, checkoutTime);

        bookRepository.saveBook(bookA);
        bookRepository.saveBook(bookB);

        service.checkForOverdueBooks();

        assertEquals(2, notificationSpy.notificationCount());
    }
}
