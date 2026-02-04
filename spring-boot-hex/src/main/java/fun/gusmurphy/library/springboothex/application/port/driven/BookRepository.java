package fun.gusmurphy.library.springboothex.application.port.driven;

import fun.gusmurphy.library.springboothex.application.Book;
import fun.gusmurphy.library.springboothex.application.Isbn;
import fun.gusmurphy.library.springboothex.application.UserId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookRepository {

    void saveBook(Book book);

    Optional<Book> findByIsbn(Isbn isbn);

    Collection<Book> findAllDueAtOrBefore(ZonedDateTime time);

    Collection<Book> booksCheckedOutBy(UserId id);
}
