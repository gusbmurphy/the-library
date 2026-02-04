package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.application.port.secondary.TellsTime;
import java.time.ZonedDateTime;

public class TestClock implements TellsTime {

    private ZonedDateTime currentTime;

    @Override
    public ZonedDateTime currentTime() {
        return currentTime;
    }

    public void setCurrentTime(ZonedDateTime currentTime) {
        this.currentTime = currentTime;
    }
}
