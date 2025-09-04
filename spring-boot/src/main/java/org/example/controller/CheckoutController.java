package org.example.controller;

import org.example.service.BookService;
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
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request) {
        var book = bookService.getBookByIsbn(request.isbn);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var checkoutSuccessful = bookService.checkoutBook(book.getIsbn(), request.userId);
        if (checkoutSuccessful) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>("Book is currently checked out.", HttpStatus.CONFLICT);
    }

    public record CheckoutRequest(String isbn, String userId) {}
}
