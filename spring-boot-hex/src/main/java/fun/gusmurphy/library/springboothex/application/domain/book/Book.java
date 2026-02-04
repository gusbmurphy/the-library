package fun.gusmurphy.library.springboothex.application.domain.book;

import fun.gusmurphy.library.springboothex.application.domain.overdue.OverdueNotification;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.port.secondary.SendsOverdueNotifications;
import java.time.ZonedDateTime;
import java.util.Optional;

public class Book {

    private final Isbn isbn;
    private final int checkoutTimeInDays;
    private ZonedDateTime checkedOutAt;
    private UserId checkedOutBy;

    public Book(
            Isbn isbn, int checkoutTimeInDays, ZonedDateTime checkedOutAt, UserId checkedOutBy) {
        this.isbn = isbn;
        this.checkoutTimeInDays = checkoutTimeInDays;
        this.checkedOutAt = checkedOutAt;
        this.checkedOutBy = checkedOutBy;
    }

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

    public ZonedDateTime checkedOutAt() {
        return checkedOutAt;
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

    public boolean isLateAsOf(ZonedDateTime time) {
        if (checkedOutAt == null) {
            return false;
        }

        return dueBackBy().orElseThrow().isEqual(time) || dueBackBy().orElseThrow().isBefore(time);
    }

    public boolean isCheckedOut() {
        return checkedOutAt != null;
    }

    public UserId checkedOutBy() {
        return checkedOutBy;
    }

    public boolean isCheckedOutBy(UserId id) {
        if (checkedOutBy == null) {
            return false;
        }

        return checkedOutBy.equals(id);
    }

    public void sendOverdueNotification(
            SendsOverdueNotifications notificationSender, ZonedDateTime asOf) {
        if (checkedOutAt == null) {
            return;
        }

        var dueDate = checkedOutAt.plusDays(checkoutTimeInDays);
        if (asOf.isEqual(dueDate) || asOf.isAfter(dueDate)) {
            var notification = new OverdueNotification(this.isbn, this.checkedOutBy, dueDate);
            notificationSender.send(notification);
        }
    }
}
