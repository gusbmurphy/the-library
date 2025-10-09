package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.BookBuilder;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class NewBookServiceTest {

    @Test
    void aReceivedBookIsPersistedToTheRepository() {
        BookRepository repository = new BookRepositoryDouble();
        ReceivesBooks service = new NewBookService(repository);
        Book newBook = new BookBuilder()
                .withIsbnString("test-isbn")
                .withCheckoutTimeInDaysInt(30)
                .build();

        service.receiveBook(newBook);

        Optional<Book> persistedBook = repository.findByIsbn(Isbn.fromString("test-isbn"));
        Assertions.assertTrue(persistedBook.isPresent());
    }

}
