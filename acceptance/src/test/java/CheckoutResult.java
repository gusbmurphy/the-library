import org.junit.jupiter.api.Assertions;

public class CheckoutResult {

    private final int responseStatusCode;

    public CheckoutResult(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public void checkoutIsSuccessful() {
        Assertions.assertEquals(200, responseStatusCode);
    }
}
