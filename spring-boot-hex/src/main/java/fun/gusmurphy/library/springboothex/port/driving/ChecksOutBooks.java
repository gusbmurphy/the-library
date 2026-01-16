package fun.gusmurphy.library.springboothex.port.driving;

import fun.gusmurphy.library.springboothex.domain.CheckoutResult;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;

public interface ChecksOutBooks {

    CheckoutResult checkoutBook(Isbn isbn, UserId userId);
}
