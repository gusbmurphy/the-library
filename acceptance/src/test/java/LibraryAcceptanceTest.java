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

    @Test
    void bookCantBeCheckedOutByTwoUsers() throws Exception {
        var book = books.newBookArrives();

        var firstUser = UserResource.newUser();
        var secondUser = UserResource.newUser();

        var firstResult = firstUser.attemptsToCheckout(book);
        firstResult.checkoutIsSuccessful();

        var secondResult = secondUser.attemptsToCheckout(book);
        secondResult.failedBecauseBookIsAlreadyCheckedOut();
    }
}
