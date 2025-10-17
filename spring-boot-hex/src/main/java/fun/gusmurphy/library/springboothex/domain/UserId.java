package fun.gusmurphy.library.springboothex.domain;

import java.util.UUID;

public class UserId {

    private final UUID uuid;

    private UserId(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String string) {
        return new UserId(UUID.fromString(string));
    }

}
