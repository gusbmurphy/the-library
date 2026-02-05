package fun.gusmurphy.library.springboothex.application.port.primary;

import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.domain.user.UserType;

public interface RegistersUsers {

    void registerUser(UserId userId, UserType type);
}
