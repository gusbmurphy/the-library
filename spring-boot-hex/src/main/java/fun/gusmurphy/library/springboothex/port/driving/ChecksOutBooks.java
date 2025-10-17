package fun.gusmurphy.library.springboothex.port.driving;

import fun.gusmurphy.library.springboothex.domain.CheckoutResult;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;

public interface ChecksOutBooks {

    CheckoutResult requestCheckout(Isbn isbn, UserId userId);
}
