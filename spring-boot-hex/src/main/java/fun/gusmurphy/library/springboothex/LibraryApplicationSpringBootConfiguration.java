package fun.gusmurphy.library.springboothex;

import fun.gusmurphy.library.springboothex.domain.CheckoutService;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.application.BookService;
import fun.gusmurphy.library.springboothex.port.driven.CheckoutRecordRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class LibraryApplicationSpringBootConfiguration {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

    @Bean
    public CheckoutService checkoutService(CheckoutRecordRepository recordRepository, BookRepository bookRepository) {
        return new CheckoutService(recordRepository, bookRepository);
    }

}
