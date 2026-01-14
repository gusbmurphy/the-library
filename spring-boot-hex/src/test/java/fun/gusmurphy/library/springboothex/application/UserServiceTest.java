package fun.gusmurphy.library.springboothex.application;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.doubles.UserRepositoryDouble;
import fun.gusmurphy.library.springboothex.port.driving.RegistersUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    private final UserRepositoryDouble userRepository = new UserRepositoryDouble();
    private final RegistersUsers service = new UserService(userRepository);

    @BeforeEach
    void setup() {
        userRepository.clear();
    }

    @Test
    void aUserCanBeRegistered() {
        var userId = UserId.random();

        service.registerUser(userId);

        assertTrue(userRepository.existsById(userId));
    }
}
