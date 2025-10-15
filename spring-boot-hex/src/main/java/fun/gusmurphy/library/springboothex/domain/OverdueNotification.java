package fun.gusmurphy.library.springboothex.domain;

import java.time.ZonedDateTime;

public record OverdueNotification(Isbn isbn, UserId userId, ZonedDateTime lateAsOf) {
}
