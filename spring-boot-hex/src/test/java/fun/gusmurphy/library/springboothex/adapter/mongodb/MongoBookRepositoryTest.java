package fun.gusmurphy.library.springboothex.adapter.mongodb;

import com.mongodb.assertions.Assertions;
import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest(
        includeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = MongoBookRepository.class))
public class MongoBookRepositoryTest {

    @Container
    static MongoDBContainer mongoContainer =
            new MongoDBContainer("mongo:7.0").withExposedPorts(27017);

    @Autowired MongoBookRepository repository;

    @DynamicPropertySource
    static void containerProperties(DynamicPropertyRegistry registry) {
        mongoContainer.start();
        registry.add("spring.data.mongodb.host", mongoContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
    }

    @Test
    void bookCanBeSaved() {
        var isbn = Isbn.fromString("123");
        var book = new Book(isbn, 30);

        repository.saveBook(book);

        var retrievedBook = repository.findByIsbn(isbn);
        Assertions.assertTrue(retrievedBook.isPresent());
    }
}
