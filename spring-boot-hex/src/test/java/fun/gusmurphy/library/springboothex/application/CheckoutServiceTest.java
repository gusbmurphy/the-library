package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.CheckoutResult;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CheckoutServiceTest {

    @Test
    void aUserCanSuccessfullyCheckoutABook() {
        CheckoutRecordRepository recordRepository = new CheckoutRepositoryDouble();
        BookRepository bookRepository = new BookRepositoryDouble();
        ChecksOutBooks service = new CheckoutService(recordRepository, bookRepository);

        var isbn = Isbn.fromString("my-isbn");
        var book = new Book(isbn, 30);
        bookRepository.saveBook(book);
        var userId = UserId.random();

        var result = service.requestCheckout(isbn, userId);

        Assertions.assertEquals(CheckoutResult.SUCCESS, result);
    }

}
