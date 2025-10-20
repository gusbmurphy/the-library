package fun.gusmurphy.library.springboothex.port.driven;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookRepository {

    void saveBook(Book book);

    Optional<Book> findByIsbn(Isbn isbn);

    Collection<Book> findAllDueAtOrBefore(ZonedDateTime time);
}
