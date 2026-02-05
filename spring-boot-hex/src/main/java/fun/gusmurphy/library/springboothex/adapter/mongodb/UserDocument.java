package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.application.domain.user.User;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.domain.user.UserType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("User")
record UserDocument(@Id String id, String type) {

    static UserDocument from(User user) {
        return new UserDocument(user.id().toString(), stringFromUserType(user.type()));
    }

    User toDomain() {
        return new User(UserId.fromString(id), userTypeFromString(type));
    }

    private static String stringFromUserType(UserType type) {
        return switch (type) {
            case REGULAR -> "r";
            case SUPER -> "s";
        };
    }

    private static UserType userTypeFromString(String string) {
        return switch (string) {
            case "r" -> UserType.REGULAR;
            case "s" -> UserType.SUPER;
            default -> throw new IllegalStateException("Unexpected user type value: " + string);
        };
    }
}
