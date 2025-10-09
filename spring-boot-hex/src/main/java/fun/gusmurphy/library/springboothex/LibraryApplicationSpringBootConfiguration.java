package fun.gusmurphy.library.springboothex;

import fun.gusmurphy.library.springboothex.application.BookRepository;
import fun.gusmurphy.library.springboothex.application.NewBookService;
import fun.gusmurphy.library.springboothex.application.ReceivesBooks;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class LibraryApplicationSpringBootConfiguration {

    @Bean
    public ReceivesBooks bookReceiver(BookRepository bookRepository) {
        return new NewBookService(bookRepository);
    }

}
