package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CheckoutServiceTest {

    private static final CheckoutRepositoryDouble recordRepository = new CheckoutRepositoryDouble();
    private static final BookRepositoryDouble bookRepository = new BookRepositoryDouble();
    private static final ChecksOutBooks service = new CheckoutService(recordRepository, bookRepository);

    private static final Isbn isbn = Isbn.fromString("my-isbn");
    private static final Book book = new Book(isbn, 30);

    @BeforeAll
    static void saveBook() {
        bookRepository.saveBook(book);
    }

    @AfterEach
    void cleanUpRecords() {
        recordRepository.clear();
    }

    @Test
    void aUserCanSuccessfullyCheckoutABook() {
        var userId = UserId.random();
        var result = service.requestCheckout(isbn, userId);
        Assertions.assertEquals(CheckoutResult.SUCCESS, result);
    }

    @Test
    void anUnknownBookCannotBeCheckedOut() {
        var userId = UserId.random();
        var someUnknownIsbn = Isbn.fromString("?");
        var result = service.requestCheckout(someUnknownIsbn, userId);
        Assertions.assertEquals(CheckoutResult.UNKNOWN_BOOK, result);
    }

    @Test
    void twoUsersCannotSimultaneouslyCheckoutABook() {
        var firstUserId = UserId.random();
        var secondUserId = UserId.random();

        service.requestCheckout(isbn, firstUserId);
        var secondResult = service.requestCheckout(isbn, secondUserId);

        Assertions.assertEquals(CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT, secondResult);
    }

}
