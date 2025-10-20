package fun.gusmurphy.library.springboothex.domain;

import java.time.ZonedDateTime;
import java.util.Optional;

public class Book {

    private final Isbn isbn;
    private final int checkoutTimeInDays;
    private ZonedDateTime checkedOutAt;

    public Book(Isbn isbn, int checkoutTimeInDays) {
        this.isbn = isbn;
        this.checkoutTimeInDays = checkoutTimeInDays;
    }

    public Isbn isbn() {
        return isbn;
    }

    public int checkoutTimeInDays() {
        return checkoutTimeInDays;
    }

    public void checkout(UserId userId, ZonedDateTime checkoutTime) {
        this.checkedOutAt = checkoutTime;
    }

    public Optional<ZonedDateTime> dueBackBy() {
        if (checkedOutAt == null) {
            return Optional.empty();
        }

        return Optional.of(checkedOutAt.plusDays(checkoutTimeInDays));
    }

    public boolean isCheckedOut() {
        return checkedOutAt != null;
    }
}
