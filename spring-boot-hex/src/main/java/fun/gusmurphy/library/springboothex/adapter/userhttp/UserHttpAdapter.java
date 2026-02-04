package fun.gusmurphy.library.springboothex.adapter.userhttp;

import fun.gusmurphy.library.springboothex.application.UserId;
import fun.gusmurphy.library.springboothex.application.port.driving.RegistersUsers;
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
        userRegistrar.registerUser(userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record RegisterUserRequest(String id) {}
}
