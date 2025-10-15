package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.OverdueNotificationSpy;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OverdueNotificationServiceTest {

    private final CheckoutRepositoryDouble checkoutRepository = new CheckoutRepositoryDouble();
    private final TestClock testClock = new TestClock();
    private final OverdueNotificationSpy notificationSpy = new OverdueNotificationSpy();

    private final ChecksForOverdueBooks service = new OverdueNotificationService(
            checkoutRepository, testClock, notificationSpy
    );

    @AfterEach
    void clearCheckoutRepository() {
        checkoutRepository.clear();
    }

    @Test
    void noNotificationsAreSentIfNothingIsOverdue() {
        service.checkForOverdueBooks();
        assertTrue(notificationSpy.noNotificationsSent());
    }

    @Test
    void aNotificationIsSentForASingleOverdueBook() {
        var currentTime = ZonedDateTime.now();
        testClock.setCurrentTime(currentTime);
        var dueBackDate = currentTime.minusDays(1);
        var isbn = Isbn.fromString("isbn");
        var userId = UserId.random();
        var record = new CheckoutRecord(isbn, userId, dueBackDate);
        checkoutRepository.saveRecord(record);

        service.checkForOverdueBooks();

        var notification = notificationSpy.latestNotification();
        assertEquals(isbn, notification.isbn());
        assertEquals(userId, notification.userId());
        assertEquals(dueBackDate, notification.lateAsOf());
    }

    @Test
    void notificationsAreSentForMultipleOverdueBooks() {
        var currentTime = ZonedDateTime.now();
        testClock.setCurrentTime(currentTime);

        var dueBackDate = currentTime.minusDays(1);
        var recordA = new CheckoutRecord(Isbn.fromString("123"), UserId.random(), dueBackDate);
        var recordB = new CheckoutRecord(Isbn.fromString("456"), UserId.random(), dueBackDate);
        checkoutRepository.saveRecord(recordA);
        checkoutRepository.saveRecord(recordB);

        service.checkForOverdueBooks();

        assertEquals(2, notificationSpy.notificationCount());
    }

}
