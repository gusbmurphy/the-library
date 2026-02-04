package fun.gusmurphy.library.springboothex;

import fun.gusmurphy.library.springboothex.domain.BookService;
import fun.gusmurphy.library.springboothex.domain.CheckoutService;
import fun.gusmurphy.library.springboothex.domain.OverdueNotificationService;
import fun.gusmurphy.library.springboothex.domain.UserService;
import fun.gusmurphy.library.springboothex.domain.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.domain.port.driven.SendsOverdueNotifications;
import fun.gusmurphy.library.springboothex.domain.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.domain.port.driven.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryApplicationSpringBootConfiguration {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public CheckoutService checkoutService(
            BookRepository bookRepository, UserRepository userRepository, TellsTime clock) {
        return new CheckoutService(bookRepository, userRepository, clock);
    }

    @Bean
    public OverdueNotificationService overdueNotificationService(
            BookRepository bookRepository,
            TellsTime clock,
            SendsOverdueNotifications notificationSender) {
        return new OverdueNotificationService(bookRepository, clock, notificationSender);
    }
}
