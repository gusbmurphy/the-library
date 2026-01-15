package fun.gusmurphy.library.acceptance;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class BasicFunctionalityTest extends LibraryAcceptanceTest {
    public BasicFunctionalityTest() {
        super();
    }

    @Test
    void unregisteredUserCannotCheckoutBook() throws Exception {
        var book = books.newBookArrives();
        var user = users.unregisteredUser();
        var result = user.attemptsToCheckout(book);
        result.userNotRegistered();
    }

    @Test
    void newBookCanBeCheckedOut() throws Exception {
        var newBook = books.newBookArrives();
        var user = users.newUser();
        var result = user.attemptsToCheckout(newBook);
        result.checkoutIsSuccessful();
    }

    @Test
    void unknownBookCannotBeCheckedOut() throws Exception {
        var unknownBook = books.unknownBook();
        var user = users.newUser();
        var result = user.attemptsToCheckout(unknownBook);
        result.bookNotFound();
    }

    @Test
    void bookCantBeCheckedOutByTwoUsers() throws Exception {
        var book = books.newBookArrives();

        var firstUser = users.newUser();
        var secondUser = users.newUser();

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
        var user = users.newUser();
        user.successfullyChecksOut(book);

        var lateThreshold = checkoutTime.plusDays(90);
        overdueNotifications.noneExistFor(book, user, lateThreshold);

        TimeTravel.to(lateThreshold);
        overdueNotifications.oneExistsFor(book, user, lateThreshold);
    }
}
