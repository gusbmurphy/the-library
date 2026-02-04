package fun.gusmurphy.library.springboothex.adapter.checkouthttp;

import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.domain.port.driving.ChecksOutBooks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutHttpAdapter {

    private final ChecksOutBooks bookCheckerOuter;

    public CheckoutHttpAdapter(ChecksOutBooks bookCheckerOuter) {
        this.bookCheckerOuter = bookCheckerOuter;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request) {
        var requestedIsbn = Isbn.fromString(request.isbn);
        var requestingUserId = UserId.fromString(request.userId);

        var result = bookCheckerOuter.checkoutBook(requestedIsbn, requestingUserId);

        return switch (result) {
            case SUCCESS -> new ResponseEntity<>(HttpStatus.OK);
            case BOOK_CURRENTLY_CHECKED_OUT ->
                    new ResponseEntity<>("Book is currently checked out.", HttpStatus.valueOf(409));
            case UNKNOWN_BOOK -> new ResponseEntity<>("Unknown book.", HttpStatus.NOT_FOUND);
            case USER_NOT_REGISTERED ->
                    new ResponseEntity<>("User is not registered.", HttpStatus.FORBIDDEN);
            case USER_AT_CHECKOUT_MAX ->
                    new ResponseEntity<>(
                            "User has too many books currently checked out.",
                            HttpStatus.METHOD_NOT_ALLOWED);
        };
    }

    public record CheckoutRequest(String isbn, String userId) {}
}
