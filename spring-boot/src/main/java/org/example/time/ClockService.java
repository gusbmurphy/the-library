package org.example.time;

import java.time.Clock;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

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
