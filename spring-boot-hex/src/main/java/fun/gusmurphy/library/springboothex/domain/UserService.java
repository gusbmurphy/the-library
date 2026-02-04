package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.UserRepository;
import fun.gusmurphy.library.springboothex.port.driving.RegistersUsers;

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
