package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;

import java.util.Optional;

public interface BookRepository {

    void saveBook(Book book);
    Optional<Book> findByIsbn(Isbn isbn);

}
