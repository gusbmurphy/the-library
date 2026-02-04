package fun.gusmurphy.library.springboothex.application.port.primary;

import fun.gusmurphy.library.springboothex.application.Book;

public interface ReceivesBooks {
    void receiveBook(Book book);
}
