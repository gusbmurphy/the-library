package fun.gusmurphy.library.springboothex.application.domain.user;

import fun.gusmurphy.library.springboothex.application.port.primary.RegistersUsers;
import fun.gusmurphy.library.springboothex.application.port.secondary.UserRepository;

public class UserService implements RegistersUsers {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserId userId, UserType type) {
        var user = new User(userId, type);
        userRepository.save(user);
    }
}
