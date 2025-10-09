package fun.gusmurphy.library.springboothex.port.driven;

import fun.gusmurphy.library.springboothex.domain.CheckoutRecord;
import fun.gusmurphy.library.springboothex.domain.Isbn;

import java.util.Optional;

public interface CheckoutRecordRepository {

    Optional<CheckoutRecord> findRecordForIsbn(Isbn isbn);
    void saveRecord(CheckoutRecord record);

}
