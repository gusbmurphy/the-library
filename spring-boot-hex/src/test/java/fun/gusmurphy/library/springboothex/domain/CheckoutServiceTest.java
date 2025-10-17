package fun.gusmurphy.library.springboothex.domain;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.adapter.persistence.ListBookRepository;
import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CheckoutServiceTest {

    private static final CheckoutRepositoryDouble recordRepository = new CheckoutRepositoryDouble();
    private static final BookRepository bookRepository = new ListBookRepository();
    private static final TestClock clock = new TestClock();
    private static final ZonedDateTime testTime = ZonedDateTime.now();
    private static final ChecksOutBooks service =
            new CheckoutService(recordRepository, bookRepository, clock);

    private static final Isbn isbn = Isbn.fromString("my-isbn");
    private static final Book book = new Book(isbn, 30);

    @BeforeAll
    static void setup() {
        bookRepository.saveBook(book);
        clock.setCurrentTime(testTime);
    }

    @AfterEach
    void cleanUpRecords() {
        recordRepository.clear();
    }

    @Test
    void aUserCanSuccessfullyCheckoutABook() {
        var userId = UserId.random();
        var result = service.requestCheckout(isbn, userId);
        assertEquals(CheckoutResult.SUCCESS, result);
    }

    @Test
    void theSavedCheckoutRecordHasTheCorrectAttributes() {
        var userId = UserId.random();
        service.requestCheckout(isbn, userId);

        var record = recordRepository.findRecordForIsbn(isbn).get();
        var expectedDueDate = testTime.plusDays(book.checkoutTimeInDays());
        assertEquals(expectedDueDate, record.dueBackDate());
        assertEquals(userId, record.userId());
        assertEquals(isbn, record.isbn());
    }

    @Test
    void anUnknownBookCannotBeCheckedOut() {
        var userId = UserId.random();
        var someUnknownIsbn = Isbn.fromString("?");
        var result = service.requestCheckout(someUnknownIsbn, userId);
        assertEquals(CheckoutResult.UNKNOWN_BOOK, result);
    }

    @Test
    void twoUsersCannotSimultaneouslyCheckoutABook() {
        var firstUserId = UserId.random();
        var secondUserId = UserId.random();

        service.requestCheckout(isbn, firstUserId);
        var secondResult = service.requestCheckout(isbn, secondUserId);

        assertEquals(CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT, secondResult);
    }
}
