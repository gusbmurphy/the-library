package fun.gusmurphy.library.springboothex.adapter.persistence;

import fun.gusmurphy.library.springboothex.domain.BookBuilder;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListRepositoryTest {

    @Test
    void booksAreRetrievableByIsbn() {
        var repo = new ListRepository();
        var testIsbn = "test-isbn";
        var book = new BookBuilder().withIsbnString(testIsbn).withCheckoutTimeInDaysInt(2).build();

        repo.saveBook(book);
        var retrievedBook = repo.findByIsbn(Isbn.fromString(testIsbn));

        assertTrue(retrievedBook.isPresent());
    }

}