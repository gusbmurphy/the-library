package fun.gusmurphy.library.springboothex.adapter.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
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
@Import(MongoConfiguration.class)
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
        var checkoutTime = ZonedDateTime.now();
        var checkoutUserId = UserId.random();
        var book = new Book(isbn, 30, checkoutTime, checkoutUserId);

        repository.saveBook(book);

        var retrievedBook = repository.findByIsbn(isbn).get();
        assertEquals(isbn, retrievedBook.isbn());
        assertEquals(30, retrievedBook.checkoutTimeInDays());
        assertEquals(
                utcTimeTruncatedToMillis(checkoutTime),
                utcTimeTruncatedToMillis(retrievedBook.checkedOutAt()));
        assertEquals(checkoutUserId, retrievedBook.checkedOutBy());
    }

    @Test
    void aBookOverdueAtTheGivenTimeIsReturnedFromTheQuery() {
        var checkoutTime = ZonedDateTime.of(1991, 12, 14, 9, 0, 0, 0, ZoneOffset.UTC);
        var checkoutTimeInDays = 14;
        var isbn = Isbn.fromString("456");
        var checkoutUserId = UserId.random();
        var book = new Book(isbn, checkoutTimeInDays, checkoutTime, checkoutUserId);

        repository.saveBook(book);

        var currentTime = checkoutTime.plusDays(14);

        var results = repository.findAllDueAtOrBefore(currentTime);
        assertEquals(1, results.size());
        assertEquals(isbn, results.stream().findFirst().get().isbn());
    }

    private static ZonedDateTime utcTimeTruncatedToMillis(ZonedDateTime zdt) {
        return zdt.withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS);
    }
}
