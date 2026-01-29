package fun.gusmurphy.library.acceptance;

import org.junit.jupiter.api.Test;

public class UserAttributesTest extends LibraryAcceptanceTest {
    public UserAttributesTest() {
        super();
    }

    @Test
    void aRegularUserCannotCheckoutMoreThan5Books() throws Exception {
        var firstBooks = books.multipleNewBooks(5);
        var user = users.newRegularUser();

        for (var book : firstBooks) {
            user.successfullyChecksOut(book);
        }

        var sixthBook = books.newBookArrives();
        var result = user.attemptsToCheckout(sixthBook);
        result.failedBecauseOfTooManyCheckouts();
    }
}
