package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.domain.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.domain.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.domain.port.driven.UserRepository;
import fun.gusmurphy.library.springboothex.domain.port.driving.ChecksOutBooks;
import java.util.stream.Collectors;

public class CheckoutService implements ChecksOutBooks {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final TellsTime clock;

    public CheckoutService(
            BookRepository bookRepository, UserRepository userRepository, TellsTime clock) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Override
    public CheckoutResult checkoutBook(Isbn isbn, UserId userId) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return CheckoutResult.USER_NOT_REGISTERED;
        }

        if (userCannotCheckoutAnotherBook(optionalUser.get())) {
            return CheckoutResult.USER_AT_CHECKOUT_MAX;
        }

        var requestTime = clock.currentTime();

        var optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isEmpty()) {
            return CheckoutResult.UNKNOWN_BOOK;
        }
        var book = optionalBook.get();

        if (book.isCheckedOut()) {
            return CheckoutResult.BOOK_CURRENTLY_CHECKED_OUT;
        }

        book.checkout(userId, requestTime);
        bookRepository.saveBook(book);

        return CheckoutResult.SUCCESS;
    }

    private boolean userCannotCheckoutAnotherBook(User user) {
        var booksCheckedOutByUser = bookRepository.booksCheckedOutBy(user.id());
        return !user.canCheckoutAnotherBook(
                booksCheckedOutByUser.stream().map(Book::isbn).collect(Collectors.toSet()));
    }
}
