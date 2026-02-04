package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.domain.OverdueNotification;
import fun.gusmurphy.library.springboothex.domain.port.driven.SendsOverdueNotifications;
import java.util.ArrayList;
import java.util.List;

public class OverdueNotificationSpy implements SendsOverdueNotifications {

    private final List<OverdueNotification> notificationList = new ArrayList<>();

    public void reset() {
        notificationList.clear();
    }

    public boolean noNotificationsSent() {
        return notificationList.isEmpty();
    }

    public OverdueNotification latestNotification() {
        return notificationList.isEmpty() ? null : notificationList.getLast();
    }

    @Override
    public void send(OverdueNotification notification) {
        notificationList.add(notification);
    }

    public int notificationCount() {
        return notificationList.size();
    }
}
