package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.application.BookRepository;
import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookRepositoryDouble implements BookRepository {

    private final List<Book> bookList = new ArrayList<>();

    @Override
    public void saveBook(Book book) {
        bookList.add(book);
    }

    @Override
    public Optional<Book> findByIsbn(Isbn isbn) {
        return bookList.stream().filter(book -> Objects.equals(book.isbn(), isbn)).findFirst();
    }

}
