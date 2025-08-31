import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

public class LibraryAcceptanceTest {

    private final BookResource books = new BookResource();
    private final UserResource user = new UserResource();

    @Test
    void newBookCanBeCheckedOut()
            throws ExecutionException, InterruptedException, IOException, URISyntaxException {
        var newBook = books.newBookArrives();
        var result = user.attemptsToCheckout(newBook);
        result.checkoutIsSuccessful();
    }
}
