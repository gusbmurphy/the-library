package fun.gusmurphy.library.springboothex.application.port.driven;

import fun.gusmurphy.library.springboothex.application.OverdueNotification;

public interface SendsOverdueNotifications {

    void send(OverdueNotification notification);
}
