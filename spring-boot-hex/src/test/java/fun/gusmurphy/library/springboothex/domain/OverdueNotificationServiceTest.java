package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.OverdueNotificationSpy;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OverdueNotificationServiceTest {

    @Test
    void noNotificationsAreSentIfNothingIsOverdue() {
        var emptyCheckoutRepository = new CheckoutRepositoryDouble();
        var testClock = new TestClock();
        var notificationSpy = new OverdueNotificationSpy();

        ChecksForOverdueBooks service = new OverdueNotificationService(
                emptyCheckoutRepository, testClock, notificationSpy
        );

        service.checkForOverdueBooks();

        Assertions.assertTrue(notificationSpy.noNotificationsSent());
    }

}
