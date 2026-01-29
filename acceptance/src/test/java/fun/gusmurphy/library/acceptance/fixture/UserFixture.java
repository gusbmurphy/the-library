package fun.gusmurphy.library.acceptance.fixture;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class UserFixture {

    public User newRegularUser() throws Exception {
        var user = User.regular();
        user.register();
        return user;
    }

    public User unregisteredUser() {
        return User.regular();
    }

    public static class User {
        private final String id;
        private final UserTypeCode typeCode;

        private User(String id, UserTypeCode typeCode) {
            this.id = id;
            this.typeCode = typeCode;
        }

        protected static User regular() {
            return new User(UUID.randomUUID().toString(), UserTypeCode.REGULAR);
        }

        public String id() {
            return id;
        }

        private void register() throws Exception {
            var requestBodyJson = registrationRequestBodyJson();

            var request =
                    HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/users"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                            .build();

            HttpResponse<String> response =
                    HttpClient.newBuilder()
                            .build()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                throw new RuntimeException(
                        "Failed to register user. Status: "
                                + response.statusCode()
                                + ", Body: "
                                + response.body());
            }
        }

        private String registrationRequestBodyJson() {
            return """
                         {
                             "id": "%s",
                             "type": "%s"
                         }
                   """
                    .formatted(id, typeCode.string());
        }

        public void successfullyChecksOut(Book book) throws Exception {
            var result = attemptsToCheckout(book);
            result.checkoutIsSuccessful();
        }

        public CheckoutResult attemptsToCheckout(Book book)
                throws IOException, URISyntaxException, InterruptedException {
            var requestBodyJson = checkoutRequestBodyFor(book);

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

        private String checkoutRequestBodyFor(Book book) {
            return String.format("{ \"isbn\": \"%s\", \"userId\": \"%s\" }", book.isbn(), id);
        }
    }

    enum UserTypeCode {
        REGULAR("1");

        private final String string;

        UserTypeCode(String string) {
            this.string = string;
        }

        String string() {
            return string;
        }
    }
}
