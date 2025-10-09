package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;

import java.util.Optional;

public interface RetrievesBooks {
    Optional<Book> retrieveBookByIsbn(Isbn isbn);
}
