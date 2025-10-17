package fun.gusmurphy.library.springboothex.adapter.overduekafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.gusmurphy.library.springboothex.domain.OverdueNotification;
import fun.gusmurphy.library.springboothex.port.driven.SendsOverdueNotifications;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOverdueNotificationAdapter implements SendsOverdueNotifications {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String OVERDUE_NOTIFICATION_TOPIC_NAME = "overdue-notifications";

    public KafkaOverdueNotificationAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void send(OverdueNotification notification) {
        var json =
                new OverdueNotificationJson(
                        notification.isbn().toString(),
                        notification.userId().toString(),
                        notification.lateAsOf().toString());

        try {
            var jsonString = objectMapper.writeValueAsString(json);
            kafkaTemplate.send(OVERDUE_NOTIFICATION_TOPIC_NAME, jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static class OverdueNotificationJson {
        private String bookIsbn;
        private String userId;
        private String lateAsOf;

        public OverdueNotificationJson(String bookIsbn, String userId, String lateAsOf) {
            this.bookIsbn = bookIsbn;
            this.userId = userId;
            this.lateAsOf = lateAsOf;
        }

        public String getBookIsbn() {
            return bookIsbn;
        }

        public String getUserId() {
            return userId;
        }

        public String getLateAsOf() {
            return lateAsOf;
        }
    }
}
