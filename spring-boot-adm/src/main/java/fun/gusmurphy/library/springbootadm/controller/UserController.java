package fun.gusmurphy.library.springbootadm.controller;

import fun.gusmurphy.library.springbootadm.domain.User;
import fun.gusmurphy.library.springbootadm.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserRequest request) {
        var user = new User();
        user.setId(request.id);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record RegisterUserRequest(String id) {}
}
