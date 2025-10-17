package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;

public class CheckoutService implements ChecksOutBooks {

    private final CheckoutRecordRepository recordRepository;
    private final BookRepository bookRepository;
    private final TellsTime clock;

    public CheckoutService(CheckoutRecordRepository recordRepository, BookRepository bookRepository, TellsTime clock) {
        this.recordRepository = recordRepository;
        this.bookRepository = bookRepository;
        this.clock = clock;
    }

    @Override
    public CheckoutResult requestCheckout(Isbn isbn, UserId userId) {
        var requestTime = clock.currentTime();

        if (recordRepository.findRecordForIsbn(isbn).isPresent()) {
            return CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT;
        }

        var optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isEmpty()) {
            return CheckoutResult.UNKNOWN_BOOK;
        }
        var book = optionalBook.get();

        var dueBackDate = requestTime.plusDays(book.checkoutTimeInDays());
        var record = new CheckoutRecord(isbn, userId, dueBackDate);
        recordRepository.saveRecord(record);

        return CheckoutResult.SUCCESS;
    }
}
