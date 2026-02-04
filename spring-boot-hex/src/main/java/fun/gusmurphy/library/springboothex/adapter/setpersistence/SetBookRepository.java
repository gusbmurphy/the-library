package fun.gusmurphy.library.springboothex.adapter.setpersistence;

import fun.gusmurphy.library.springboothex.application.domain.book.Book;
import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.port.secondary.BookRepository;
import java.time.ZonedDateTime;
import java.util.*;

public class SetBookRepository implements BookRepository {

    protected final Set<Book> bookSet = new HashSet<>();

    @Override
    public void saveBook(Book book) {
        bookSet.add(book);
    }

    @Override
    public Optional<Book> findByIsbn(Isbn isbn) {
        return bookSet.stream().filter(book -> Objects.equals(book.isbn(), isbn)).findFirst();
    }

    @Override
    public Collection<Book> findAllDueAtOrBefore(ZonedDateTime time) {
        return bookSet.stream().filter(book -> book.isLateAsOf(time)).toList();
    }

    @Override
    public Collection<Book> booksCheckedOutBy(UserId id) {
        return bookSet.stream().filter(book -> book.isCheckedOutBy(id)).toList();
    }
}
