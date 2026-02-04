package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.application.port.secondary.BookRepository;
import fun.gusmurphy.library.springboothex.application.port.secondary.SendsOverdueNotifications;
import fun.gusmurphy.library.springboothex.application.port.secondary.TellsTime;
import fun.gusmurphy.library.springboothex.application.port.primary.ChecksForOverdueBooks;

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
