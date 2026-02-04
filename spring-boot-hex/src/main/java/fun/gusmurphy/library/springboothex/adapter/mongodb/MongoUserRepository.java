package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.application.User;
import fun.gusmurphy.library.springboothex.application.UserId;
import fun.gusmurphy.library.springboothex.application.port.secondary.UserRepository;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoUserRepository implements UserRepository {

    private final MongoTemplate template;

    public MongoUserRepository(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public void save(User user) {
        var document = UserDocument.from(user);
        template.save(document);
    }

    @Override
    public Optional<User> findById(UserId id) {
        var document = Optional.ofNullable(template.findById(id.toString(), UserDocument.class));
        return document.map(UserDocument::toDomain);
    }

    @Override
    public boolean existsById(UserId userId) {
        return template.findById(userId.toString(), UserDocument.class) != null;
    }
}
