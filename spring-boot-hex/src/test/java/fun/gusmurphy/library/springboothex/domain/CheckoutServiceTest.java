package fun.gusmurphy.library.springboothex.domain;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.doubles.UserRepositoryDouble;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;
import java.time.ZonedDateTime;
import java.util.UUID;
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
        saveBookWithIsbn(isbn);
        clock.setCurrentTime(testTime);
    }

    @Test
    void aRegisteredUserCanSuccessfullyCheckoutABook() {
        var userId = registerNewUser();
        var result = service.checkoutBook(isbn, userId);
        assertEquals(CheckoutResult.SUCCESS, result);
    }

    @Test
    void anUnregisteredUserCannotCheckoutABook() {
        var userId = UserId.random();
        var result = service.checkoutBook(isbn, userId);
        assertEquals(CheckoutResult.USER_NOT_REGISTERED, result);
    }

    @Test
    void theSavedCheckoutRecordHasTheCorrectAttributes() {
        var userId = registerNewUser();
        service.checkoutBook(isbn, userId);

        var bookAfterCheckout = bookRepository.findByIsbn(isbn).get();
        var expectedDueDate = testTime.plusDays(checkoutTimeInDays);
        assertEquals(expectedDueDate, bookAfterCheckout.dueBackBy().get());
        assertEquals(userId, bookAfterCheckout.checkedOutBy());
    }

    @Test
    void anUnknownBookCannotBeCheckedOut() {
        var userId = registerNewUser();
        var someUnknownIsbn = Isbn.fromString("?");
        var result = service.checkoutBook(someUnknownIsbn, userId);
        assertEquals(CheckoutResult.UNKNOWN_BOOK, result);
    }

    @Test
    void twoUsersCannotSimultaneouslyCheckoutABook() {
        var firstUserId = registerNewUser();
        var secondUserId = registerNewUser();

        service.checkoutBook(isbn, firstUserId);
        var secondResult = service.checkoutBook(isbn, secondUserId);

        assertEquals(CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT, secondResult);
    }

    @Test
    void aRegularUserCannotCheckoutMoreThan5Books() {
        var userId = registerNewUser();
        for (int i = 0; i < 5; i++) {
            var book = saveNewBook();
            service.checkoutBook(book.isbn(), userId);
        }

        var sixthBook = saveNewBook();
        var sixthResult = service.checkoutBook(sixthBook.isbn(), userId);

        assertEquals(CheckoutResult.USER_AT_CHECKOUT_MAX, sixthResult);
    }

    private UserId registerNewUser() {
        var id = UserId.random();
        userRepository.save(new User(id));
        return id;
    }

    private Book saveNewBook() {
        var book = new Book(Isbn.fromString(UUID.randomUUID().toString()), checkoutTimeInDays);
        bookRepository.saveBook(book);
        return book;
    }

    private void saveBookWithIsbn(Isbn isbn) {
        var book = new Book(isbn, checkoutTimeInDays);
        bookRepository.saveBook(book);
    }
}
