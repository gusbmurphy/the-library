import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserResource {

    public static User newUser() {
        return new User();
    }

    public static class User {
        public CheckoutResult attemptsToCheckout(Book book)
                throws IOException, URISyntaxException, InterruptedException {
            var requestBodyJson = String.format("{ \"isbn\": \"%s\" }", book.isbn());

            var request =
                    HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/checkout"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                            .build();

            HttpResponse<String> response =
                    HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

            return new CheckoutResult(response.statusCode());
        }
    }
}
