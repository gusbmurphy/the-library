package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.example.domain.CheckoutRecord;
import org.example.domain.OverdueNotification;
import org.example.repository.CheckoutRecordRepository;
import org.example.time.ClockService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OverdueNotificationService {

    private final ClockService clockService;
    private final BookService bookService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CheckoutRecordRepository checkoutRecordRepository;
    private static final String TOPIC_NAME = "overdue-notifications";
    private static final ObjectWriter OVERDUE_MESSAGE_WRITER = setupObjectWriter();
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public OverdueNotificationService(
            ClockService clockService,
            BookService bookService,
            KafkaTemplate<String, String> kafkaTemplate,
            CheckoutRecordRepository checkoutRecordRepository) {
        this.clockService = clockService;
        this.bookService = bookService;
        this.kafkaTemplate = kafkaTemplate;
        this.checkoutRecordRepository = checkoutRecordRepository;
    }

    private static ObjectWriter setupObjectWriter() {
        return JsonMapper.builder().build().writer();
    }

    @Scheduled(fixedDelay = 500)
    public void checkForOverdueBooks() throws JsonProcessingException {
        var currentTime = clockService.currentTime();

        var checkoutRecords = checkoutRecordRepository.findAll();
        for (var record : checkoutRecords) {
            if (bookIsOverdue(record, currentTime)) {
                var notification = new OverdueNotification();
                notification.setBookIsbn(record.getBook().getIsbn());
                notification.setUserId(record.getUserId());
                notification.setLateAsOf(
                        record.getCheckoutTime()
                                .plus(Duration.ofDays(record.getBook().getCheckoutTimeInDays()))
                                .format(DATE_TIME_FORMATTER));
                var message = OVERDUE_MESSAGE_WRITER.writeValueAsString(notification);
                kafkaTemplate.send(TOPIC_NAME, message);
            }
        }
    }

    private static boolean bookIsOverdue(CheckoutRecord record, ZonedDateTime currentTime) {
        return record.getCheckoutTime()
                        .plus(Duration.ofDays(record.getBook().getCheckoutTimeInDays()))
                        .isBefore(currentTime)
                || record.getCheckoutTime()
                        .plus(Duration.ofDays(record.getBook().getCheckoutTimeInDays()))
                        .isEqual(currentTime);
    }
}
