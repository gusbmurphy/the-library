package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.adapter.setpersistence.SetBookRepository;
import fun.gusmurphy.library.springboothex.application.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.application.port.driving.ReceivesBooks;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookServiceTest {

    @Test
    void aReceivedBookIsPersistedToTheRepository() {
        BookRepository repository = new SetBookRepository();
        ReceivesBooks service = new BookService(repository);
        Book newBook =
                new BookBuilder().withIsbnString("test-isbn").withCheckoutTimeInDaysInt(30).build();

        service.receiveBook(newBook);

        Optional<Book> persistedBook = repository.findByIsbn(Isbn.fromString("test-isbn"));
        Assertions.assertTrue(persistedBook.isPresent());
    }
}
