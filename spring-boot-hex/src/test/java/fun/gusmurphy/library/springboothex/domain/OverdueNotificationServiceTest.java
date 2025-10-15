package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.doubles.CheckoutRepositoryDouble;
import fun.gusmurphy.library.springboothex.doubles.OverdueNotificationSpy;
import fun.gusmurphy.library.springboothex.doubles.TestClock;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OverdueNotificationServiceTest {

    private final CheckoutRepositoryDouble checkoutRepository = new CheckoutRepositoryDouble();
    private final TestClock testClock = new TestClock();
    private final OverdueNotificationSpy notificationSpy = new OverdueNotificationSpy();

    private final ChecksForOverdueBooks service = new OverdueNotificationService(
            checkoutRepository, testClock, notificationSpy
    );

    @Test
    void noNotificationsAreSentIfNothingIsOverdue() {
        service.checkForOverdueBooks();
        Assertions.assertTrue(notificationSpy.noNotificationsSent());
    }

}
