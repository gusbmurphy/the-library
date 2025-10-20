package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.adapter.persistence.SetCheckoutRecordRepository;

public class CheckoutRepositoryDouble extends SetCheckoutRecordRepository {

    public void clear() {
        recordList.clear();
    }
}
