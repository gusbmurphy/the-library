package fun.gusmurphy.library.springboothex.adapter.timehttp;

import java.time.ZonedDateTime;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// In a "real" application, we would probably limit the environments that this
// adapter is present in, we would not want this in production.
@RestController
public class TimeTravelHttpAdapter {

    private final FixedClock clock;

    public TimeTravelHttpAdapter(FixedClock clock) {
        this.clock = clock;
    }

    @PutMapping("/set-clock")
    void setClock(@RequestParam("time") ZonedDateTime newTime) {
        clock.setCurrentTime(newTime);
    }
}
