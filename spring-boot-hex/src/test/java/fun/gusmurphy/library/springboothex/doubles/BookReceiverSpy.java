package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.port.driving.ReceivesBooks;
import fun.gusmurphy.library.springboothex.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookReceiverSpy implements ReceivesBooks {

    private final List<Book> receivedBooks = new ArrayList<>();

    @Override
    public void receiveBook(Book book) {
        receivedBooks.add(book);
    }

    public Book lastReceivedBook() {
        return receivedBooks.getLast();
    }
}
