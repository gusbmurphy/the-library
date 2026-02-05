package fun.gusmurphy.library.springboothex.application.domain.checkout;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.application.domain.book.Book;
import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.user.User;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.domain.user.UserType;
import fun.gusmurphy.library.springboothex.application.port.primary.ChecksOutBooks;
import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.doubles.UserRepositoryDouble;
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
        var userId = registerNewRegularUser();
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
        var userId = registerNewRegularUser();
        service.checkoutBook(isbn, userId);

        var bookAfterCheckout = bookRepository.findByIsbn(isbn).get();
        var expectedDueDate = testTime.plusDays(checkoutTimeInDays);
        assertEquals(expectedDueDate, bookAfterCheckout.dueBackBy().get());
        assertEquals(userId, bookAfterCheckout.checkedOutBy());
    }

    @Test
    void anUnknownBookCannotBeCheckedOut() {
        var userId = registerNewRegularUser();
        var someUnknownIsbn = Isbn.fromString("?");
        var result = service.checkoutBook(someUnknownIsbn, userId);
        assertEquals(CheckoutResult.UNKNOWN_BOOK, result);
    }

    @Test
    void twoUsersCannotSimultaneouslyCheckoutABook() {
        var firstUserId = registerNewRegularUser();
        var secondUserId = registerNewRegularUser();

        service.checkoutBook(isbn, firstUserId);
        var secondResult = service.checkoutBook(isbn, secondUserId);

        assertEquals(CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT, secondResult);
    }

    @Test
    void aRegularUserCannotCheckoutMoreThan5Books() {
        var userId = registerNewRegularUser();
        successfullyCheckoutNBooks(5, userId);

        var sixthBook = saveNewBook();
        var sixthResult = service.checkoutBook(sixthBook.isbn(), userId);

        assertEquals(CheckoutResult.USER_AT_CHECKOUT_MAX, sixthResult);
    }

    @Test
    void aSuperUserCannotCheckoutMoreThan8Books() {
        var userId = registerNewSuperUser();
        successfullyCheckoutNBooks(8, userId);

        var ninthBook = saveNewBook();
        var ninthResult = service.checkoutBook(ninthBook.isbn(), userId);

        assertEquals(CheckoutResult.USER_AT_CHECKOUT_MAX, ninthResult);
    }

    private UserId registerNewRegularUser() {
        var id = UserId.random();
        userRepository.save(new User(id, UserType.REGULAR));
        return id;
    }

    private UserId registerNewSuperUser() {
        var id = UserId.random();
        userRepository.save(new User(id, UserType.SUPER));
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

    private void successfullyCheckoutNBooks(int n, UserId userId) {
        for (int i = 0; i < n; i++) {
            var book = saveNewBook();
            var result = service.checkoutBook(book.isbn(), userId);
            assertEquals(CheckoutResult.SUCCESS, result, "User should be able to checkout a " + (i + 1) + "th book");
        }
    }
}
