package fun.gusmurphy.library.springboothex.application.domain.overdue;

import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import java.time.ZonedDateTime;

public record OverdueNotification(Isbn isbn, UserId userId, ZonedDateTime lateAsOf) {}
