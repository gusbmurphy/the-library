package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.domain.CheckoutRecord;
import fun.gusmurphy.library.springboothex.domain.CheckoutResult;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;

public class CheckoutService implements ChecksOutBooks {

    private final CheckoutRecordRepository recordRepository;

    public CheckoutService(CheckoutRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public CheckoutResult requestCheckout(Isbn isbn, UserId userId) {
        if (recordRepository.findRecordForIsbn(isbn).isPresent()) {
            return CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT;
        }

        var record = new CheckoutRecord(isbn);
        recordRepository.saveRecord(record);

        return CheckoutResult.SUCCESS;
    }
}
