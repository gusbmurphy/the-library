package fun.gusmurphy.library.acceptance;

import fun.gusmurphy.library.acceptance.fixture.UserFixture;
import org.junit.jupiter.api.Test;

public class UserAttributesTest extends LibraryAcceptanceTest {
    public UserAttributesTest() {
        super();
    }

    @Test
    void usersHaveCheckoutMaxesBasedOnTheirType() throws Exception {
        expectUserCanOnlyCheckoutNBooks(users.newRegularUser(), 5);
    }

    @Test
    void superUsersCanCheckoutUpTo8Books() throws Exception {
        expectUserCanOnlyCheckoutNBooks(users.newSuperUser(), 8);
    }

    private void expectUserCanOnlyCheckoutNBooks(UserFixture.User user, int n) throws Exception {
        var firstBooks = books.multipleNewBooks(n);

        for (var book : firstBooks) {
            user.successfullyChecksOut(book);
        }

        var bookPastLimit = books.newBookArrives();
        var result = user.attemptsToCheckout(bookPastLimit);
        result.failedBecauseOfTooManyCheckouts();
    }
}
