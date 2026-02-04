package fun.gusmurphy.library.springboothex.adapter.mongodb;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.application.domain.user.User;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
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
                        classes = MongoUserRepository.class))
@Import(MongoConfiguration.class)
public class MongoUserRepositoryTest {

    @Container
    static MongoDBContainer mongoContainer =
            new MongoDBContainer("mongo:7.0").withExposedPorts(27017);

    @Autowired MongoUserRepository repository;

    @DynamicPropertySource
    static void containerProperties(DynamicPropertyRegistry registry) {
        mongoContainer.start();
        registry.add("spring.data.mongodb.host", mongoContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
    }

    @Test
    void userCanBeSaved() {
        var userId = UserId.random();
        var user = new User(userId);

        repository.save(user);

        assertTrue(repository.existsById(userId));
    }

    @Test
    void existsByIdReturnsFalseForNonExistentUser() {
        var nonExistentUserId = UserId.random();

        assertFalse(repository.existsById(nonExistentUserId));
    }
}
