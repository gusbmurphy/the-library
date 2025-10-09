package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.CheckoutResult;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;

public class CheckoutService implements ChecksOutBooks {

    public CheckoutService(CheckoutRecordRepository recordRepository, BookRepository bookRepository) {
    }

    @Override
    public CheckoutResult requestCheckout(Isbn isbn, UserId userId) {
        return CheckoutResult.SUCCESS;
    }
}
