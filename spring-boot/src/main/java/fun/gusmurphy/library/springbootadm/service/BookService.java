package fun.gusmurphy.library.springbootadm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.gusmurphy.library.springbootadm.domain.Book;
import fun.gusmurphy.library.springbootadm.domain.CheckoutRecord;
import fun.gusmurphy.library.springbootadm.repository.BookRepository;
import fun.gusmurphy.library.springbootadm.repository.CheckoutRecordRepository;
import fun.gusmurphy.library.springbootadm.time.ClockService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClockService clockService;
    private final CheckoutRecordRepository checkoutRecordRepository;
    private final BookRepository bookRepository;

    public BookService(
            ClockService clockService,
            CheckoutRecordRepository checkoutRecordRepository,
            BookRepository bookRepository) {
        this.clockService = clockService;
        this.checkoutRecordRepository = checkoutRecordRepository;
        this.bookRepository = bookRepository;
    }

    @KafkaListener(id = "spring-application", topics = "book-arrivals")
    public void listenForNewBooks(String message) throws JsonProcessingException {
        var book = objectMapper.readValue(message, Book.class);
        bookRepository.save(book);
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn).orElse(null);
    }

    public boolean checkoutBook(String isbn, String userId) {
        var checkoutRecordsForBook = checkoutRecordRepository.findByBookIsbn(isbn);

        if (!checkoutRecordsForBook.isEmpty()) {
            return false;
        }

        var book = bookRepository.getReferenceById(isbn);
        var record = new CheckoutRecord(userId, book, clockService.currentTime());
        checkoutRecordRepository.save(record);
        return true;
    }
}
