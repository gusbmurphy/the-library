package fun.gusmurphy.library.springboothex.application.domain.checkout;

public enum CheckoutResult {
    SUCCESS,
    BOOK_CURRENTLY_CHECKED_OUT,
    UNKNOWN_BOOK,
    USER_AT_CHECKOUT_MAX,
    USER_NOT_REGISTERED
}
