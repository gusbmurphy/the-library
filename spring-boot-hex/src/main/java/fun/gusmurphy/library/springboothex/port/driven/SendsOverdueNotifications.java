package fun.gusmurphy.library.springboothex.port.driven;

import fun.gusmurphy.library.springboothex.domain.OverdueNotification;

public interface SendsOverdueNotifications {

    void send(OverdueNotification notification);
}
