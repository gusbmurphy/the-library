package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    private final BookService bookService;

    public CheckoutController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {
        var book = bookService.getBookByIsbn(request.isbn);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CheckoutResponse(), HttpStatus.OK);
    }

    public record CheckoutResponse() {
    }

    public record CheckoutRequest(String isbn) {
    }

}
