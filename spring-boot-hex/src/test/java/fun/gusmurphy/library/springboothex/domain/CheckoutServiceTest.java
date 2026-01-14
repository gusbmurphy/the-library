package fun.gusmurphy.library.springboothex.domain;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.doubles.UserRepositoryDouble;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckoutServiceTest {

    private final BookRepositoryDouble bookRepository = new BookRepositoryDouble();
    private final UserRepositoryDouble userRepository = new UserRepositoryDouble();
    private final TestClock clock = new TestClock();
    private final ZonedDateTime testTime = ZonedDateTime.now();
    private final int checkoutTimeInDays = 30;
    private final ChecksOutBooks service =
            new CheckoutService(bookRepository, userRepository, clock);

    private final Isbn isbn = Isbn.fromString("my-isbn");

    @BeforeEach
    void setup() {
        bookRepository.clear();
        userRepository.clear();
        var book = new Book(isbn, checkoutTimeInDays);
        bookRepository.saveBook(book);
        clock.setCurrentTime(testTime);
    }

    @Test
    void aRegisteredUserCanSuccessfullyCheckoutABook() {
        var userId = registerNewUser();
        var result = service.requestCheckout(isbn, userId);
        assertEquals(CheckoutResult.SUCCESS, result);
    }

    @Test
    void anUnregisteredUserCannotCheckoutABook() {
        var userId = UserId.random();
        var result = service.requestCheckout(isbn, userId);
        assertEquals(CheckoutResult.USER_NOT_REGISTERED, result);
    }

    @Test
    void theSavedCheckoutRecordHasTheCorrectAttributes() {
        var userId = registerNewUser();
        service.requestCheckout(isbn, userId);

        var bookAfterCheckout = bookRepository.findByIsbn(isbn).get();
        var expectedDueDate = testTime.plusDays(checkoutTimeInDays);
        assertEquals(expectedDueDate, bookAfterCheckout.dueBackBy().get());
        assertEquals(userId, bookAfterCheckout.checkedOutBy());
    }

    @Test
    void anUnknownBookCannotBeCheckedOut() {
        var userId = registerNewUser();
        var someUnknownIsbn = Isbn.fromString("?");
        var result = service.requestCheckout(someUnknownIsbn, userId);
        assertEquals(CheckoutResult.UNKNOWN_BOOK, result);
    }

    @Test
    void twoUsersCannotSimultaneouslyCheckoutABook() {
        var firstUserId = registerNewUser();
        var secondUserId = registerNewUser();

        service.requestCheckout(isbn, firstUserId);
        var secondResult = service.requestCheckout(isbn, secondUserId);

        assertEquals(CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT, secondResult);
    }

    private UserId registerNewUser() {
        var id = UserId.random();
        userRepository.save(new User(id));
        return id;
    }
}
