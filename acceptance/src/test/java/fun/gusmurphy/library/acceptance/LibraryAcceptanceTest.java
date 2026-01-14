package fun.gusmurphy.library.acceptance;

import fun.gusmurphy.library.acceptance.fixture.BookFixture;
import fun.gusmurphy.library.acceptance.fixture.OverdueNotificationFixture;
import fun.gusmurphy.library.acceptance.fixture.UserFixture;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LibraryAcceptanceTest {

    private final BookFixture books = new BookFixture();
    private final OverdueNotificationFixture overdueNotifications =
            new OverdueNotificationFixture();

    @Nested
    class BasicFunctionality {
        @Test
        void unregisteredUserCannotCheckoutBook() throws Exception {
            var book = books.newBookArrives();
            var user = UserFixture.unregisteredUser();
            var result = user.attemptsToCheckout(book);
            result.userNotRegistered();
        }

        @Test
        void newBookCanBeCheckedOut() throws Exception {
            var newBook = books.newBookArrives();
            var user = UserFixture.newUser();
            var result = user.attemptsToCheckout(newBook);
            result.checkoutIsSuccessful();
        }

        @Test
        void unknownBookCannotBeCheckedOut() throws Exception {
            var unknownBook = books.unknownBook();
            var user = UserFixture.newUser();
            var result = user.attemptsToCheckout(unknownBook);
            result.bookNotFound();
        }

        @Test
        void bookCantBeCheckedOutByTwoUsers() throws Exception {
            var book = books.newBookArrives();

            var firstUser = UserFixture.newUser();
            var secondUser = UserFixture.newUser();

            var firstResult = firstUser.attemptsToCheckout(book);
            firstResult.checkoutIsSuccessful();

            var secondResult = secondUser.attemptsToCheckout(book);
            secondResult.failedBecauseBookIsAlreadyCheckedOut();
        }

        @Test
        void aMessageIsSentWhenABookIsOverdue() throws Exception {
            var checkoutTime = ZonedDateTime.of(1991, 12, 14, 12, 0, 0, 0, ZoneId.systemDefault());
            TimeTravel.to(checkoutTime);

            var book = books.newBookWith().checkoutTimeInDays(90).create();
            var user = UserFixture.newUser();
            user.successfullyChecksOut(book);

            var lateThreshold = checkoutTime.plusDays(90);
            overdueNotifications.noneExistFor(book, user, lateThreshold);

            TimeTravel.to(lateThreshold);
            overdueNotifications.oneExistsFor(book, user, lateThreshold);
        }
    }
}
