package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.application.domain.user.User;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("User")
record UserDocument(@Id String id) {

    static UserDocument from(User user) {
        return new UserDocument(user.id().toString());
    }

    User toDomain() {
        return new User(UserId.fromString(id));
    }
}
