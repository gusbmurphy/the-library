package fun.gusmurphy.library.springbootadm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.gusmurphy.library.springbootadm.domain.Book;
import fun.gusmurphy.library.springbootadm.domain.CheckoutRecord;
import fun.gusmurphy.library.springbootadm.domain.OverdueNotification;
import fun.gusmurphy.library.springbootadm.repository.CheckoutRecordRepository;
import fun.gusmurphy.library.springbootadm.time.ClockService;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class OverdueNotificationServiceTest {

    @Mock ClockService clockService;
    @Mock CheckoutRecordRepository checkoutRecordRepository;
    @Mock KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks OverdueNotificationService service;

    @Captor ArgumentCaptor<String> messageCaptor;

    @Test
    void checkForOverdueBooks() throws JsonProcessingException {
        var currentTestTime = ZonedDateTime.now();
        when(clockService.currentTime()).thenReturn(currentTestTime);

        var book1 = new Book();
        book1.setIsbn("isbn1");
        book1.setCheckoutTimeInDays(5);

        var book2 = new Book();
        book2.setIsbn("isbn2");
        book2.setCheckoutTimeInDays(5);

        var nonOverdueRecord = new CheckoutRecord("user1", book1, currentTestTime.minusDays(1));
        var overdueRecord = new CheckoutRecord("user2", book2, currentTestTime.minusMonths(30));

        when(checkoutRecordRepository.findAll())
                .thenReturn(List.of(nonOverdueRecord, overdueRecord));

        service.checkForOverdueBooks();

        verify(kafkaTemplate, times(1)).send(eq("overdue-notifications"), messageCaptor.capture());
        var sentStringMessage = messageCaptor.getValue();

        var mapper = new ObjectMapper();
        var sentMessage = mapper.readValue(sentStringMessage, OverdueNotification.class);
        var dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        assertEquals("isbn2", sentMessage.getBookIsbn());
        assertEquals("user2", sentMessage.getUserId());
        assertEquals(
                currentTestTime.minusMonths(30).plusDays(5).format(dateTimeFormatter),
                sentMessage.getLateAsOf());
    }
}
