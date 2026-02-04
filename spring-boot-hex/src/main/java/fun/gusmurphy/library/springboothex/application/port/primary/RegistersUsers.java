package fun.gusmurphy.library.springboothex.application.port.primary;

import fun.gusmurphy.library.springboothex.application.domain.user.UserId;

public interface RegistersUsers {

    void registerUser(UserId userId);
}
