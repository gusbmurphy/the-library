package fun.gusmurphy.library.springboothex.adapter.timehttp;

import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class FixedClock implements TellsTime {

    private ZonedDateTime currentTime;

    public FixedClock() {
        this.currentTime = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.systemDefault());
    }

    @Override
    public ZonedDateTime currentTime() {
        return currentTime;
    }

    public void setCurrentTime(ZonedDateTime newCurrentTime) {
        currentTime = newCurrentTime;
    }
}
