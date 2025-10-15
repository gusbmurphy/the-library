package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;

public class OverdueNotificationService implements ChecksForOverdueBooks {

    private final CheckoutRecordRepository recordRepository;
    private final TellsTime clock;
    private final SendsOverdueNotifications notificationSender;

    public OverdueNotificationService(
            CheckoutRecordRepository recordRepository,
            TellsTime clock,
            SendsOverdueNotifications notificationSender
    ) {
        this.recordRepository = recordRepository;
        this.clock = clock;
        this.notificationSender = notificationSender;
    }

    @Override
    public void checkForOverdueBooks() {
        var overdueRecords = recordRepository.findRecordsDueBefore(clock.currentTime());

        for (var record : overdueRecords) {
            notificationSender.send(new OverdueNotification());
        }
    }
}
