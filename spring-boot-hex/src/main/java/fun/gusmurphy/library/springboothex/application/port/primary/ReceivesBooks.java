package fun.gusmurphy.library.springboothex.application.port.primary;

import fun.gusmurphy.library.springboothex.application.domain.book.Book;

public interface ReceivesBooks {
    void receiveBook(Book book);
}
