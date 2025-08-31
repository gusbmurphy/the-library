import org.junit.jupiter.api.Assertions;

public class CheckoutResult {

    private final int responseStatusCode;

    public CheckoutResult(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public void checkoutIsSuccessful() {
        Assertions.assertEquals(
                200,
                responseStatusCode,
                "Expected 200 status indicating book was successfully checked out.");
    }

    public void bookNotFound() {
        Assertions.assertEquals(
                404, responseStatusCode, "Expected 404 status indicating book is not known.");
        // TODO: Maybe there should be more to this...
    }
}
