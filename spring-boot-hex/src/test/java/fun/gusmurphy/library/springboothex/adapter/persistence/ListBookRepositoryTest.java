package fun.gusmurphy.library.springboothex.adapter.persistence;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.domain.BookBuilder;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import org.junit.jupiter.api.Test;

class ListBookRepositoryTest {

    @Test
    void booksAreRetrievableByIsbn() {
        var repo = new ListBookRepository();
        var testIsbn = "test-isbn";
        var book = new BookBuilder().withIsbnString(testIsbn).withCheckoutTimeInDaysInt(2).build();

        repo.saveBook(book);
        var retrievedBook = repo.findByIsbn(Isbn.fromString(testIsbn));

        assertTrue(retrievedBook.isPresent());
    }
}
