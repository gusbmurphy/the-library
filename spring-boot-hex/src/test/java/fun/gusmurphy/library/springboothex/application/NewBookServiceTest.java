package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.doubles.BookRepositoryDouble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class NewBookServiceTest {

    @Test
    void aReceivedBookIsPersistedToTheRepository() {
        BookRepository repository = new BookRepositoryDouble();
        ReceivesBooks service = new NewBookService(repository);
        Book newBook = new Book("test-isbn", 30);

        service.receiveBook(newBook);

        Optional<Book> persistedBook = repository.findByIsbn("test-isbn");
        Assertions.assertTrue(persistedBook.isPresent());
    }

}
