import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutResult {

    private final int responseStatusCode;
    private final String message;

    public CheckoutResult(int responseStatusCode, String message) {
        this.responseStatusCode = responseStatusCode;
        this.message = message;
    }

    public void checkoutIsSuccessful() {
        assertEquals(
                200,
                responseStatusCode,
                "Expected 200 status indicating book was successfully checked out.");
    }

    public void bookNotFound() {
        assertEquals(404, responseStatusCode, "Expected 404 status indicating book is not known.");
        assertEquals("Unknown book.", message, "Expected message indicating book is not known.");
    }

    public void failedBecauseBookIsAlreadyCheckedOut() {
        assertEquals(
                409,
                responseStatusCode,
                "Expected 409 status indicating checkout couldn't proceed.");
        assertEquals(
                "Book is currently checked out.",
                message,
                "Expected message indicating book is checked out.");
    }
}
