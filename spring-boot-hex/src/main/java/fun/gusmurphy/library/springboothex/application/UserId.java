package fun.gusmurphy.library.springboothex.application;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(uuid, userId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
