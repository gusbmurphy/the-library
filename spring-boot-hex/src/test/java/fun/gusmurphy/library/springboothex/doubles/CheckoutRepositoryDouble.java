package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.domain.CheckoutRecord;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckoutRepositoryDouble implements CheckoutRecordRepository {

    private final List<CheckoutRecord> recordList = new ArrayList<>();

    public void clear() {
        recordList.clear();
    }

    @Override
    public Optional<CheckoutRecord> findRecordForIsbn(Isbn isbn) {
        return recordList.stream().filter(r -> r.isbn().equals(isbn)).findFirst();
    }

    @Override
    public void saveRecord(CheckoutRecord record) {
        recordList.add(record);
    }
}
