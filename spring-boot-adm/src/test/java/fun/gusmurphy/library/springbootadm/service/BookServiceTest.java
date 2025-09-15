package fun.gusmurphy.library.springbootadm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fun.gusmurphy.library.springbootadm.domain.Book;
import fun.gusmurphy.library.springbootadm.domain.CheckoutRecord;
import fun.gusmurphy.library.springbootadm.repository.BookRepository;
import fun.gusmurphy.library.springbootadm.repository.CheckoutRecordRepository;
import fun.gusmurphy.library.springbootadm.time.ClockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock ClockService clockService;
    @Mock CheckoutRecordRepository checkoutRecordRepository;
    @Mock BookRepository bookRepository;

    @Captor ArgumentCaptor<Book> bookCaptor;
    @Captor ArgumentCaptor<CheckoutRecord> checkoutRecordCaptor;

    @InjectMocks BookService service;

    @Test
    void listenForNewBooksSavesToRepository() throws JsonProcessingException {
        var message =
                """
            {
                "isbn": "some-isbn",
                "checkoutTimeInDays": 7
            }
        """;

        service.listenForNewBooks(message);
        verify(bookRepository, times(1)).save(bookCaptor.capture());

        var capturedBook = bookCaptor.getValue();
        assertEquals("some-isbn", capturedBook.getIsbn());
        assertEquals(7, capturedBook.getCheckoutTimeInDays());
    }

    @Test
    void getBookByIsbnGetsABookFromTheRepository() {
        var returnedBook = new Book();
        when(bookRepository.findBookByIsbn("my-isbn")).thenReturn(Optional.of(returnedBook));

        var result = service.getBookByIsbn("my-isbn");

        assertEquals(returnedBook, result);
    }

    @Test
    void checkoutBookReturnsFalseIfARecordOfCheckoutIsFound() {
        when(checkoutRecordRepository.findByBookIsbn("my-isbn")).thenReturn(List.of(new CheckoutRecord()));

        var result = service.checkoutBook("my-isbn", "user-id");

        assertFalse(result);
    }

    @Test
    void checkingOutAnAvailableBookCreatesARecordInTheRepository() {
        when(checkoutRecordRepository.findByBookIsbn("my-isbn")).thenReturn(Collections.emptyList());

        var bookFromRepo = new Book();
        when(bookRepository.getReferenceById("my-isbn")).thenReturn(bookFromRepo);

        var testTime = ZonedDateTime.now();
        when(clockService.currentTime()).thenReturn(testTime);

        var result = service.checkoutBook("my-isbn", "user-id");

        verify(checkoutRecordRepository, times(1)).save(checkoutRecordCaptor.capture());
        var savedRecord = checkoutRecordCaptor.getValue();

        assertTrue(result);
        assertEquals(bookFromRepo, savedRecord.getBook());
        assertEquals("user-id", savedRecord.getUserId());
        assertEquals(testTime, savedRecord.getCheckoutTime());
    }
}
