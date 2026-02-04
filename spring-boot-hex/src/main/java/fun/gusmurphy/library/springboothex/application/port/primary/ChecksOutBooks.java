package fun.gusmurphy.library.springboothex.application.port.primary;

import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.checkout.CheckoutResult;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;

public interface ChecksOutBooks {

    CheckoutResult checkoutBook(Isbn isbn, UserId userId);
}
