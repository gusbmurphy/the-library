package fun.gusmurphy.library.springboothex.application.port.secondary;

import java.time.ZonedDateTime;

public interface TellsTime {
    ZonedDateTime currentTime();
}
