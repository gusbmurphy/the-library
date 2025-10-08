package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;

public class BookService implements ReceivesBooks {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void receiveBook(Book book) {
        repository.saveBook(book);
    }
}
