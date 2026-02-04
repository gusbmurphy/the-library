package fun.gusmurphy.library.springboothex.application.port.secondary;

import fun.gusmurphy.library.springboothex.application.domain.book.Book;
import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookRepository {

    void saveBook(Book book);

    Optional<Book> findByIsbn(Isbn isbn);

    Collection<Book> findAllDueAtOrBefore(ZonedDateTime time);

    Collection<Book> booksCheckedOutBy(UserId id);
}
