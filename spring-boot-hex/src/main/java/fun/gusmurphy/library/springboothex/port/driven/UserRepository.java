package fun.gusmurphy.library.springboothex.port.driven;

import fun.gusmurphy.library.springboothex.domain.User;
import fun.gusmurphy.library.springboothex.domain.UserId;

public interface UserRepository {

    void save(User user);

    boolean existsById(UserId userId);
}
