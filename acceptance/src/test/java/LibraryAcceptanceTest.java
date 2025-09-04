import org.junit.jupiter.api.Test;

public class LibraryAcceptanceTest {

    private final BookResource books = new BookResource();
    private final UserResource user = new UserResource();

    @Test
    void newBookCanBeCheckedOut() throws Exception {
        var newBook = books.newBookArrives();
        var result = user.attemptsToCheckout(newBook);
        result.checkoutIsSuccessful();
    }

    @Test
    void unknownBookCannotBeCheckedOut() throws Exception {
        var unkownBook = books.unknownBook();
        var result = user.attemptsToCheckout(unkownBook);
        result.bookNotFound();
    }
}
