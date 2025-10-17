package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.adapter.persistence.ListCheckoutRecordRepository;

public class CheckoutRepositoryDouble extends ListCheckoutRecordRepository {

    public void clear() {
        recordList.clear();
    }
}
