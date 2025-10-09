package fun.gusmurphy.library.springboothex.adapter.http;

import fun.gusmurphy.library.springboothex.port.driving.RetrievesBooks;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutHttpAdapter {

    private final RetrievesBooks bookRetriever;

    public CheckoutHttpAdapter(RetrievesBooks bookRetriever) {
        this.bookRetriever = bookRetriever;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request) {
        var requestedIsbn = Isbn.fromString(request.isbn());
        var requestedBook = bookRetriever.retrieveBookByIsbn(requestedIsbn);

        if (requestedBook.isEmpty()) {
            return new ResponseEntity<>("Unknown book.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public record CheckoutRequest(String isbn, String userId) {}
}
