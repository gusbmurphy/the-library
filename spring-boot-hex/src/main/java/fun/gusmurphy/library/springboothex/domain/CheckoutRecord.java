package fun.gusmurphy.library.springboothex.domain;

import java.time.ZonedDateTime;

public record CheckoutRecord(Isbn isbn, UserId userId, ZonedDateTime dueBackDate) {}
