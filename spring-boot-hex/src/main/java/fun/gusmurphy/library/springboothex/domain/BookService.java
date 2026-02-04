package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.domain.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.domain.port.driving.ReceivesBooks;
import fun.gusmurphy.library.springboothex.domain.port.driving.RetrievesBooks;
import java.util.Optional;

public class BookService implements ReceivesBooks, RetrievesBooks {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void receiveBook(Book book) {
        repository.saveBook(book);
    }

    @Override
    public Optional<Book> retrieveBookByIsbn(Isbn isbn) {
        return repository.findByIsbn(isbn);
    }
}
