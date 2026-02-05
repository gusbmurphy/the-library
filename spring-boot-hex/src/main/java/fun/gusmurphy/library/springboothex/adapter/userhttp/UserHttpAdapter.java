package fun.gusmurphy.library.springboothex.adapter.userhttp;

import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.domain.user.UserType;
import fun.gusmurphy.library.springboothex.application.port.primary.RegistersUsers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHttpAdapter {

    private final RegistersUsers userRegistrar;

    public UserHttpAdapter(RegistersUsers userRegistrar) {
        this.userRegistrar = userRegistrar;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserRequest request) {
        var userId = UserId.fromString(request.id());
        var userType = userTypeFromString(request.type());
        userRegistrar.registerUser(userId, userType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record RegisterUserRequest(String id, String type) {}

    private static UserType userTypeFromString(String string) {
        return switch (string) {
            case "1" -> UserType.REGULAR;
            case "S" -> UserType.SUPER;
            default -> throw new IllegalStateException("Unexpected user type: " + string);
        };
    }
}
