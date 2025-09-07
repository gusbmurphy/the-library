package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.domain.Book;
import org.example.domain.CheckoutRecord;
import org.example.time.ClockService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final List<Book> knownBooks = new ArrayList<>();
    private final List<CheckoutRecord> checkoutRecords = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClockService clockService;

    public BookService(ClockService clockService) {
        this.clockService = clockService;
    }

    @KafkaListener(id = "spring-application", topics = "book-arrivals")
    public void listenForNewBooks(String message) throws JsonProcessingException {
        var book = objectMapper.readValue(message, Book.class);
        knownBooks.add(book);
    }

    public Book getBookByIsbn(String isbn) {
        return knownBooks.stream()
                .filter(book -> Objects.equals(book.getIsbn(), isbn))
                .findFirst()
                .orElse(null);
    }

    public boolean checkoutBook(String isbn, String userId) {
        if (checkoutRecords.stream().anyMatch(cr -> Objects.equals(cr.book().getIsbn(), isbn))) {
            return false;
        }

        var book =
                knownBooks.stream()
                        .filter(b -> Objects.equals(b.getIsbn(), isbn))
                        .findFirst()
                        .get();
        checkoutRecords.add(new CheckoutRecord(userId, book, clockService.currentTime()));
        return true;
    }

    public List<CheckoutRecord> getCheckoutRecords() {
        return checkoutRecords;
    }
}
