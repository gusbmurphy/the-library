package org.example.time;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

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
