package fun.gusmurphy.library.springboothex.adapter.setpersistence;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.application.domain.book.BookBuilder;
import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import org.junit.jupiter.api.Test;

class SetBookRepositoryTest {

    @Test
    void booksAreRetrievableByIsbn() {
        var repo = new SetBookRepository();
        var testIsbn = "test-isbn";
        var book = new BookBuilder().withIsbnString(testIsbn).withCheckoutTimeInDaysInt(2).build();

        repo.saveBook(book);
        var retrievedBook = repo.findByIsbn(Isbn.fromString(testIsbn));

        assertTrue(retrievedBook.isPresent());
    }
}
