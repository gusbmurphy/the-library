package fun.gusmurphy.library.springboothex.application;

import fun.gusmurphy.library.springboothex.application.port.secondary.UserRepository;
import fun.gusmurphy.library.springboothex.application.port.primary.RegistersUsers;

public class UserService implements RegistersUsers {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserId userId) {
        var user = new User(userId);
        userRepository.save(user);
    }
}
