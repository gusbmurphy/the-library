package fun.gusmurphy.library.springboothex.application;

import java.time.ZonedDateTime;

public record CheckoutRecord(Isbn isbn, UserId userId, ZonedDateTime dueBackDate) {}
