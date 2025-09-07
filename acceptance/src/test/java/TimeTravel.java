import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeTravel {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static void to(ZonedDateTime checkoutTime)
            throws URISyntaxException, IOException, InterruptedException {
        var uriString =
                "http://localhost:8080/set-clock/" + checkoutTime.format(DATE_TIME_FORMATTER);
        var request =
                HttpRequest.newBuilder()
                        .uri(new URI(uriString))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.noBody())
                        .build();

        var response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Was not able to set clock on application for time travel.");
    }
}
