package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.port.driving.ChecksForOverdueBooks;

public class OverdueNotificationService implements ChecksForOverdueBooks {

    private final BookRepository bookRepository;
    private final TellsTime clock;
    private final SendsOverdueNotifications notificationSender;

    public OverdueNotificationService(
            BookRepository bookRepository,
            TellsTime clock,
            SendsOverdueNotifications notificationSender) {
        this.bookRepository = bookRepository;
        this.clock = clock;
        this.notificationSender = notificationSender;
    }

    @Override
    public void checkForOverdueBooks() {
        var currentTime = clock.currentTime();
        var books = bookRepository.findAllDueAtOrBefore(currentTime);

        for (var book : books) {
            book.sendOverdueNotification(notificationSender, currentTime);
        }
    }
}
