package fun.gusmurphy.library.springboothex.adapter.timehttp;

import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class FixedClock implements TellsTime {

    private ZonedDateTime currentTime;

    public FixedClock() {
        this.currentTime = ZonedDateTime.of(LocalDateTime.of(1800, 5, 9, 9, 0), ZoneId.systemDefault());
    }

    @Override
    public ZonedDateTime currentTime() {
        return currentTime;
    }

    public void setCurrentTime(ZonedDateTime newCurrentTime) {
        currentTime = newCurrentTime;
    }
}
