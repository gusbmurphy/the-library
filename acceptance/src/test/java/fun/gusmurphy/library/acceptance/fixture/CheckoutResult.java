package fun.gusmurphy.library.acceptance.fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.opentest4j.AssertionFailedError;

public class CheckoutResult {
    private static final int MAX_RETRIES = 5;
    private static final Duration RETRY_TIMEOUT = Duration.ofMillis(100);

    private final int responseStatusCode;
    private final String message;
    private final CheckoutRetryFunction retryFunction;
    private int retryCount = 0;

    public CheckoutResult(
            int responseStatusCode, String message, CheckoutRetryFunction retryFunction) {
        this.responseStatusCode = responseStatusCode;
        this.message = message;
        this.retryFunction = retryFunction;
    }

    @FunctionalInterface
    public interface CheckoutRetryFunction {
        CheckoutResult retry() throws Exception;
    }

    public void checkoutIsSuccessful() throws Exception {
        try {
            assertEquals(
                    200,
                    responseStatusCode,
                    "Expected 200 status indicating book was successfully checked out.");
        } catch (AssertionFailedError failure) {
            if (retryCount < MAX_RETRIES) {
                retryForSuccessfulCheckout();
            } else {
                throw failure;
            }
        }
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

    private void retryForSuccessfulCheckout() throws Exception {
        Thread.sleep(RETRY_TIMEOUT);
        var retriedResult = retryFunction.retry();
        retriedResult.setRetryCount(retryCount + 1);
        retriedResult.checkoutIsSuccessful();
    }

    private void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
