package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;

public class OverdueNotificationSpy implements SendsOverdueNotifications {
    public boolean noNotificationsSent() {
        return true;
    }
}
