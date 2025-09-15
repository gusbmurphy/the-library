package fun.gusmurphy.library.acceptance.fixture;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class UserFixture {

    public static User newUser() {
        return new User();
    }

    public static class User {
        private final String id;

        public User() {
            id = UUID.randomUUID().toString();
        }

        public String id() {
            return id;
        }

        public void successfullyChecksOut(Book book) throws Exception {
            var result = attemptsToCheckout(book);
            result.checkoutIsSuccessful();
        }

        public CheckoutResult attemptsToCheckout(Book book)
                throws IOException, URISyntaxException, InterruptedException {
            var requestBodyJson = createRequestBodyFor(book);

            var request =
                    HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/checkout"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                            .build();

            HttpResponse<String> response =
                    HttpClient.newBuilder()
                            .build()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            return new CheckoutResult(
                    response.statusCode(), response.body(), () -> this.attemptsToCheckout(book));
        }

        private String createRequestBodyFor(Book book) {
            return String.format("{ \"isbn\": \"%s\", \"userId\": \"%s\" }", book.isbn(), id);
        }
    }
}
