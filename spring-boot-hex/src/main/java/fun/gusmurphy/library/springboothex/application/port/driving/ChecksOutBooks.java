package fun.gusmurphy.library.springboothex.application.port.driving;

import fun.gusmurphy.library.springboothex.application.CheckoutResult;
import fun.gusmurphy.library.springboothex.application.Isbn;
import fun.gusmurphy.library.springboothex.application.UserId;

public interface ChecksOutBooks {

    CheckoutResult checkoutBook(Isbn isbn, UserId userId);
}
