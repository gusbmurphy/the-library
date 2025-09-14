package fun.gusmurphy.library.springbootadm.time;

import java.time.ZonedDateTime;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClockController {

    ClockService clockService;

    public ClockController(ClockService clockService) {
        this.clockService = clockService;
    }

    @PutMapping("/set-clock")
    void setClock(@RequestParam("time") ZonedDateTime newTime) {
        clockService.setTime(newTime);
    }
}
