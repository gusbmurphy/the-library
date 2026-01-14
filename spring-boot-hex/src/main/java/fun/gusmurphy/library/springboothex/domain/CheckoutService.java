package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.port.driven.UserRepository;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;

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
    public CheckoutResult requestCheckout(Isbn isbn, UserId userId) {
        if (!userRepository.existsById(userId)) {
            return CheckoutResult.USER_NOT_REGISTERED;
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
}
