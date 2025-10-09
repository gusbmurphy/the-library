package fun.gusmurphy.library.springboothex;

import fun.gusmurphy.library.springboothex.application.BookRepository;
import fun.gusmurphy.library.springboothex.application.BookService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class LibraryApplicationSpringBootConfiguration {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

}
