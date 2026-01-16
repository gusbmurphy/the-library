package fun.gusmurphy.library.springboothex.port.driven;

import fun.gusmurphy.library.springboothex.domain.User;
import fun.gusmurphy.library.springboothex.domain.UserId;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(UserId id);

    boolean existsById(UserId userId);
}
