package fun.gusmurphy.library.springboothex.application.port.secondary;

import fun.gusmurphy.library.springboothex.application.domain.overdue.OverdueNotification;

public interface SendsOverdueNotifications {

    void send(OverdueNotification notification);
}
