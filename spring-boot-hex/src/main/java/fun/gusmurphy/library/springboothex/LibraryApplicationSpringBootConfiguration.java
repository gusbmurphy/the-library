package fun.gusmurphy.library.springboothex;

import fun.gusmurphy.library.springboothex.application.BookService;
import fun.gusmurphy.library.springboothex.domain.CheckoutService;
import fun.gusmurphy.library.springboothex.domain.OverdueNotificationService;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class LibraryApplicationSpringBootConfiguration {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

    @Bean
    public CheckoutService checkoutService(
            CheckoutRecordRepository recordRepository,
            BookRepository bookRepository,
            TellsTime clock) {
        return new CheckoutService(recordRepository, bookRepository, clock);
    }

    @Bean
    public OverdueNotificationService overdueNotificationService(
            CheckoutRecordRepository checkoutRecordRepository,
            TellsTime clock,
            SendsOverdueNotifications notificationSender) {
        return new OverdueNotificationService(checkoutRecordRepository, clock, notificationSender);
    }
}
