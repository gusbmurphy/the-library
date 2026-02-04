package fun.gusmurphy.library.springboothex.application.port.driven;

import java.time.ZonedDateTime;

public interface TellsTime {
    ZonedDateTime currentTime();
}
