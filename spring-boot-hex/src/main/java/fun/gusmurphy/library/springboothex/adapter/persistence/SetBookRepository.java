package fun.gusmurphy.library.springboothex.adapter.persistence;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SetBookRepository implements BookRepository {

    protected final Set<Book> bookList = new HashSet<>();

    @Override
    public void saveBook(Book book) {
        bookList.add(book);
    }

    @Override
    public Optional<Book> findByIsbn(Isbn isbn) {
        return bookList.stream().filter(book -> Objects.equals(book.isbn(), isbn)).findFirst();
    }
}
