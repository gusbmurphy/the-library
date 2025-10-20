package fun.gusmurphy.library.springboothex.domain;

import java.time.ZonedDateTime;
import java.util.Optional;

public class Book {

    private final Isbn isbn;
    private final int checkoutTimeInDays;
    private ZonedDateTime checkedOutAt;
    private UserId checkedOutBy;

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
        this.checkedOutBy = userId;
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

    public UserId checkedOutBy() {
        return checkedOutBy;
    }
}
