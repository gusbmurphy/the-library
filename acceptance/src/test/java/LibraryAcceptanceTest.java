import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

public class LibraryAcceptanceTest {

    private final BookResource books = new BookResource();
    private final OverdueNotifications overdueNotifications = new OverdueNotifications();

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

    @Test
    void aMessageIsSentWhenABookIsOverdue() throws Exception {
        var checkoutTime = ZonedDateTime.of(1991, 12, 14, 12, 0, 0, 0, ZoneId.systemDefault());
        TimeTravel.to(checkoutTime);

        var book = books.newBookWith().checkoutTimeInDays(90).create();
        var user = UserResource.newUser();
        user.successfullyChecksOut(book);

        var lateThreshold = checkoutTime.plusDays(90);
        TimeTravel.to(lateThreshold);
        overdueNotifications.oneExistsFor(book, user, lateThreshold);
    }
}
