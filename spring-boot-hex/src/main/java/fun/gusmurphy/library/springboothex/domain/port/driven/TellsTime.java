package fun.gusmurphy.library.springboothex.domain.port.driven;

import java.time.ZonedDateTime;

public interface TellsTime {
    ZonedDateTime currentTime();
}
