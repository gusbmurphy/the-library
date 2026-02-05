package fun.gusmurphy.library.springboothex.application.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import fun.gusmurphy.library.springboothex.application.port.primary.RegistersUsers;
import fun.gusmurphy.library.springboothex.doubles.UserRepositoryDouble;
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

        service.registerUser(userId, UserType.REGULAR);

        var registeredUser = userRepository.findById(userId).orElseThrow();
        assertEquals(userId, registeredUser.id());
        assertEquals(UserType.REGULAR, registeredUser.type());
    }
}
