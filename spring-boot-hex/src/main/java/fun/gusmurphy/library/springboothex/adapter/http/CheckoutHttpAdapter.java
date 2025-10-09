package fun.gusmurphy.library.springboothex.adapter.http;

import fun.gusmurphy.library.springboothex.domain.UserId;
import fun.gusmurphy.library.springboothex.port.driving.ChecksOutBooks;
import fun.gusmurphy.library.springboothex.domain.Isbn;
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

        var result = bookCheckerOuter.requestCheckout(requestedIsbn, requestingUserId);
        
        return switch (result) {
            case SUCCESS -> new ResponseEntity<>(HttpStatus.OK);
            case BOOK_CURRENTLY_CHECKED_OUT -> new ResponseEntity<>("Book is currently checked out.", HttpStatus.valueOf(409));
            case UNKNOWN_BOOK -> new ResponseEntity<>("Unknown book.", HttpStatus.NOT_FOUND);
        };
    }

    public record CheckoutRequest(String isbn, String userId) {}
}
