package fun.gusmurphy.library.springboothex.application.domain.user;

import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import java.util.Collection;

public class User {

    private final UserId id;
    private final UserType type;

    public User(UserId id, UserType type) {
        this.id = id;
        this.type = type;
    }

    public UserId id() {
        return id;
    }

    public UserType type() {
        return type;
    }

    public boolean canCheckoutAnotherBook(Collection<Isbn> currentlyCheckedOutIsbns) {
        if (type == UserType.SUPER) {
            return currentlyCheckedOutIsbns.size() < 8;
        }
        return currentlyCheckedOutIsbns.size() < 5;
    }
}
