package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;

public class OverdueNotificationService implements ChecksForOverdueBooks {

    public OverdueNotificationService(
            CheckoutRecordRepository recordRepository,
            TellsTime clock,
            SendsOverdueNotifications notificationSender
    ) {
    }

    @Override
    public void checkForOverdueBooks() {
    }
}
