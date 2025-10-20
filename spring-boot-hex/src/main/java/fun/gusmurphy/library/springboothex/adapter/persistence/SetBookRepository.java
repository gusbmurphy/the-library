package fun.gusmurphy.library.springboothex.adapter.persistence;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import java.time.ZonedDateTime;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
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
        return bookSet.stream()
                .filter(
                        book -> {
                            var optionalDueBackDate = book.dueBackBy();
                            if (optionalDueBackDate.isEmpty()) {
                                return false;
                            }

                            var dueBackDate = optionalDueBackDate.get();
                            return dueBackDate.isEqual(time) || dueBackDate.isBefore(time);
                        })
                .toList();
    }
}
