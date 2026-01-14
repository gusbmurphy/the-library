package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.domain.User;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.port.driven.UserRepository;
import java.util.HashSet;
import java.util.Set;

public class UserRepositoryDouble implements UserRepository {

    private final Set<UserId> users = new HashSet<>();

    @Override
    public void save(User user) {
        users.add(user.id());
    }

    @Override
    public boolean existsById(UserId userId) {
        return users.contains(userId);
    }

    public void clear() {
        users.clear();
    }
}
