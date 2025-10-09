package fun.gusmurphy.library.springboothex.domain;

import java.util.UUID;

public class UserId {

    private final UUID uuid;

    private UserId(UUID uuid) {
        this.uuid = uuid;
    }

    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }

}
