package fun.gusmurphy.library.springboothex.application.port.primary;

import fun.gusmurphy.library.springboothex.application.domain.book.Book;
import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import java.util.Optional;

public interface RetrievesBooks {
    Optional<Book> retrieveBookByIsbn(Isbn isbn);
}
