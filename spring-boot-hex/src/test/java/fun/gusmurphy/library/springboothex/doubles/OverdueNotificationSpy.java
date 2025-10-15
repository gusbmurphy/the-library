package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.domain.OverdueNotification;
import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;

import java.util.ArrayList;
import java.util.List;

public class OverdueNotificationSpy implements SendsOverdueNotifications {

    private final List<OverdueNotification> notificationList = new ArrayList<>();

    public boolean noNotificationsSent() {
        return true;
    }

    public OverdueNotification latestNotification() {
        return notificationList.isEmpty() ? null : notificationList.getLast();
    }

    @Override
    public void send(OverdueNotification notification) {
        notificationList.add(notification);
    }
}
