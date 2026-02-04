package fun.gusmurphy.library.springboothex.application.port.driving;

import fun.gusmurphy.library.springboothex.application.Book;
import fun.gusmurphy.library.springboothex.application.Isbn;
import java.util.Optional;

public interface RetrievesBooks {
    Optional<Book> retrieveBookByIsbn(Isbn isbn);
}
