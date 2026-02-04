package fun.gusmurphy.library.springboothex.application.domain.checkout;

import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import java.time.ZonedDateTime;

public record CheckoutRecord(Isbn isbn, UserId userId, ZonedDateTime dueBackDate) {}
