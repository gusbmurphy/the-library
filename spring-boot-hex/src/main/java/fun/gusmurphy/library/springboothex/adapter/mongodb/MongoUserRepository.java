package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.domain.User;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.port.driven.UserRepository;
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
    public boolean existsById(UserId userId) {
        return template.findById(userId.toString(), UserDocument.class) != null;
    }
}
