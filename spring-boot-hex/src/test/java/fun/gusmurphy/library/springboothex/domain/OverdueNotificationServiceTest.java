package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.OverdueNotificationSpy;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OverdueNotificationServiceTest {

    private final CheckoutRepositoryDouble checkoutRepository = new CheckoutRepositoryDouble();
    private final TestClock testClock = new TestClock();
    private final OverdueNotificationSpy notificationSpy = new OverdueNotificationSpy();
    private static final ZonedDateTime TEST_TIME = ZonedDateTime.now();

    private final ChecksForOverdueBooks service = new OverdueNotificationService(
            checkoutRepository, testClock, notificationSpy
    );

    @AfterEach
    void clearCheckoutRepository() {
        checkoutRepository.clear();
    }

    @Test
    void noNotificationsAreSentIfNothingIsOverdue() {
        service.checkForOverdueBooks();
        assertTrue(notificationSpy.noNotificationsSent());
    }

    @ParameterizedTest
    @MethodSource("dueBackDatesThatShouldBeOverdueByTestTime")
    void aNotificationIsSentForASingleOverdueBook(ZonedDateTime dueBackDate) {
        testClock.setCurrentTime(TEST_TIME);
        var isbn = Isbn.fromString("isbn");
        var userId = UserId.random();
        var record = new CheckoutRecord(isbn, userId, dueBackDate);
        checkoutRepository.saveRecord(record);

        service.checkForOverdueBooks();

        var notification = notificationSpy.latestNotification();
        assertEquals(isbn, notification.isbn());
        assertEquals(userId, notification.userId());
        assertEquals(dueBackDate, notification.lateAsOf());
    }

    private static Stream<Arguments> dueBackDatesThatShouldBeOverdueByTestTime() {
        return Stream.of(
                Arguments.of(TEST_TIME),
                Arguments.of(TEST_TIME.minusDays(1))
        );
    }

    @Test
    void notificationsAreSentForMultipleOverdueBooks() {
        testClock.setCurrentTime(TEST_TIME);

        var dueBackDate = TEST_TIME.minusDays(1);
        var recordA = new CheckoutRecord(Isbn.fromString("123"), UserId.random(), dueBackDate);
        var recordB = new CheckoutRecord(Isbn.fromString("456"), UserId.random(), dueBackDate);
        checkoutRepository.saveRecord(recordA);
        checkoutRepository.saveRecord(recordB);

        service.checkForOverdueBooks();

        assertEquals(2, notificationSpy.notificationCount());
    }

}
