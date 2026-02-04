package fun.gusmurphy.library.springboothex.application;

import java.util.Collection;

public class User {

    private final UserId id;

    public User(UserId id) {
        this.id = id;
    }

    public UserId id() {
        return id;
    }

    public boolean canCheckoutAnotherBook(Collection<Isbn> currentlyCheckedOutIsbns) {
        return currentlyCheckedOutIsbns.size() < 5;
    }
}
