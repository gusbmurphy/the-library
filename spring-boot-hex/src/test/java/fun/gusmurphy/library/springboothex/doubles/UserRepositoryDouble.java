package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.domain.User;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.domain.port.driven.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepositoryDouble implements UserRepository {

    private final Set<User> users = new HashSet<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return users.stream().filter(user -> user.id().equals(id)).findFirst();
    }

    @Override
    public boolean existsById(UserId userId) {
        return users.stream().anyMatch(user -> user.id().equals(userId));
    }

    public void clear() {
        users.clear();
    }
}
