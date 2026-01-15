package fun.gusmurphy.library.acceptance;

import fun.gusmurphy.library.acceptance.fixture.BookFixture;
import fun.gusmurphy.library.acceptance.fixture.OverdueNotificationFixture;
import fun.gusmurphy.library.acceptance.fixture.UserFixture;

abstract class LibraryAcceptanceTest {

    final BookFixture books = new BookFixture();
    final UserFixture users = new UserFixture();
    final OverdueNotificationFixture overdueNotifications = new OverdueNotificationFixture();
}
