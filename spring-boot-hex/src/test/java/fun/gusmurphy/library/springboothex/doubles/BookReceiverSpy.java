package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.application.Book;
import fun.gusmurphy.library.springboothex.application.port.primary.ReceivesBooks;
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
