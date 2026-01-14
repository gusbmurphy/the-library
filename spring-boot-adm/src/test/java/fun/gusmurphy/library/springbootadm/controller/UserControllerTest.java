package fun.gusmurphy.library.springbootadm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fun.gusmurphy.library.springbootadm.controller.UserController.RegisterUserRequest;
import fun.gusmurphy.library.springbootadm.domain.User;
import fun.gusmurphy.library.springbootadm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock UserRepository userRepository;

    @Captor ArgumentCaptor<User> userCaptor;

    @InjectMocks UserController controller;

    @Test
    void registerUserSavesUserToRepository() {
        var request = new RegisterUserRequest("test-user-id");

        controller.registerUser(request);

        verify(userRepository, times(1)).save(userCaptor.capture());
        var savedUser = userCaptor.getValue();
        assertEquals("test-user-id", savedUser.getId());
    }

    @Test
    void registerUserReturns201Created() {
        var request = new RegisterUserRequest("test-user-id");

        var response = controller.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
