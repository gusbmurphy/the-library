package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.domain.Book;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private List<Book> knownBooks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
}
