package fun.gusmurphy.library.springbootadm.controller;

import fun.gusmurphy.library.springbootadm.repository.CheckoutRecordRepository;
import fun.gusmurphy.library.springbootadm.repository.UserRepository;
import fun.gusmurphy.library.springbootadm.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    private static final int REGULAR_CHECKOUT_LIMIT = 5;
    private static final int SUPER_CHECKOUT_LIMIT = 8;

    private final BookService bookService;
    private final UserRepository userRepository;
    private final CheckoutRecordRepository checkoutRecordRepository;

    public CheckoutController(
            BookService bookService,
            UserRepository userRepository,
            CheckoutRecordRepository checkoutRecordRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.checkoutRecordRepository = checkoutRecordRepository;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request) {
        var user = userRepository.findById(request.userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User is not registered.", HttpStatus.FORBIDDEN);
        }

        var checkoutLimit =
                "S".equals(user.get().getType()) ? SUPER_CHECKOUT_LIMIT : REGULAR_CHECKOUT_LIMIT;
        if (checkoutRecordRepository.countByUserId(request.userId) >= checkoutLimit) {
            return new ResponseEntity<>(
                    "User has too many books currently checked out.",
                    HttpStatus.METHOD_NOT_ALLOWED);
        }

        var book = bookService.getBookByIsbn(request.isbn);
        if (book == null) {
            return new ResponseEntity<>("Unknown book.", HttpStatus.NOT_FOUND);
        }

        var checkoutSuccessful = bookService.checkoutBook(book.getIsbn(), request.userId);
        if (checkoutSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>("Book is currently checked out.", HttpStatus.CONFLICT);
    }

    public record CheckoutRequest(String isbn, String userId) {}
}
