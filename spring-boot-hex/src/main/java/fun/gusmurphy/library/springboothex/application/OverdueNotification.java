package fun.gusmurphy.library.springboothex.application;

import java.time.ZonedDateTime;

public record OverdueNotification(Isbn isbn, UserId userId, ZonedDateTime lateAsOf) {}
