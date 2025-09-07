package org.example.time;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;

@Service
public class ClockService {

    private Clock clock = Clock.systemDefaultZone();

    protected void setTime(ZonedDateTime time) {
        clock = Clock.fixed(time.toInstant(), time.getZone());
    }

    public ZonedDateTime currentTime() {
        return ZonedDateTime.now(clock);
    }

}
