package fun.gusmurphy.library.springboothex.application.port.secondary;

import fun.gusmurphy.library.springboothex.application.OverdueNotification;

public interface SendsOverdueNotifications {

    void send(OverdueNotification notification);
}
