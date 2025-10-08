package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;

import java.util.Optional;

public interface BookRepository {

    void saveBook(Book book);
    Optional<Book> findByIsbn(String isbn);

}
