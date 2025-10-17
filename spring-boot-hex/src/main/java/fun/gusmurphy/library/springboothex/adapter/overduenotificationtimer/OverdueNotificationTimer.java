package fun.gusmurphy.library.springboothex.adapter.overduenotificationtimer;

import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueNotificationTimer {

    private final ChecksForOverdueBooks overdueChecker;

    public OverdueNotificationTimer(ChecksForOverdueBooks overdueChecker) {
        this.overdueChecker = overdueChecker;
    }

    @Scheduled(fixedDelay = 100)
    public void triggerCheckForOverdueBooks() {
        overdueChecker.checkForOverdueBooks();
    }
}
