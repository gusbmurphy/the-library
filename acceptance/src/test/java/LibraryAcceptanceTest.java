import org.junit.jupiter.api.Test;

public class LibraryAcceptanceTest {

    private final BookResource books = new BookResource();

    @Test
    void newBookCanBeCheckedOut() throws Exception {
        var newBook = books.newBookArrives();
        var user = UserResource.newUser();
        var result = user.attemptsToCheckout(newBook);
        result.checkoutIsSuccessful();
    }

    @Test
    void unknownBookCannotBeCheckedOut() throws Exception {
        var unknownBook = books.unknownBook();
        var user = UserResource.newUser();
        var result = user.attemptsToCheckout(unknownBook);
        result.bookNotFound();
    }
}
