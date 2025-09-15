package fun.gusmurphy.library.springbootadm.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import fun.gusmurphy.library.springbootadm.domain.Book;
import fun.gusmurphy.library.springbootadm.repository.BookRepository;
import fun.gusmurphy.library.springbootadm.repository.CheckoutRecordRepository;
import fun.gusmurphy.library.springbootadm.time.ClockService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock ClockService clockService;
    @Mock CheckoutRecordRepository checkoutRecordRepository;
    @Mock BookRepository bookRepository;

    @Captor ArgumentCaptor<Book> bookCaptor;

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
        Assertions.assertEquals("some-isbn", capturedBook.getIsbn());
        Assertions.assertEquals(7, capturedBook.getCheckoutTimeInDays());
    }
}
