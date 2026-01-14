package fun.gusmurphy.library.springboothex.domain;

public class User {

    private final UserId id;

    public User(UserId id) {
        this.id = id;
    }

    public UserId id() {
        return id;
    }
}
