package fun.gusmurphy.library.springboothex.application.port.driven;

import fun.gusmurphy.library.springboothex.application.User;
import fun.gusmurphy.library.springboothex.application.UserId;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(UserId id);

    boolean existsById(UserId userId);
}
