package fun.gusmurphy.library.springboothex.domain;

import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import fun.gusmurphy.library.springboothex.port.driven.TellsTime;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;

public class CheckoutService implements ChecksOutBooks {

    private final BookRepository bookRepository;
    private final TellsTime clock;

    public CheckoutService(BookRepository bookRepository, TellsTime clock) {
        this.bookRepository = bookRepository;
        this.clock = clock;
    }

    @Override
    public CheckoutResult requestCheckout(Isbn isbn, UserId userId) {
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
