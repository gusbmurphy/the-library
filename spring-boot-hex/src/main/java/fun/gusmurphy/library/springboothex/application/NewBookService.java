package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.Book;

public class NewBookService implements ReceivesBooks {

    private final BookRepository repository;

    public NewBookService(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public void receiveBook(Book book) {
        repository.saveBook(book);
    }
}
