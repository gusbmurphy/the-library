import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;

public class OverdueNotifications {

    private final KafkaConsumer<String, String> kafkaConsumer;
    private static final String OVERDUE_TOPIC_NAME = "overdue-notifications";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public OverdueNotifications() {
        var props = createConsumerProperties();
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(List.of(OVERDUE_TOPIC_NAME));
    }

    public void oneExistsFor(Book book, UserResource.User user, ZonedDateTime lateThreshold)
            throws JsonProcessingException {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

        for (var record : records) {
            var message = MAPPER.readValue(record.value(), OverdueNotificationMessage.class);
            if (message.isFor(book, user, lateThreshold)) {
                return;
            }
        }

        Assertions.fail(notificationNotFoundMessageFor(book, user, lateThreshold));
    }

    private String notificationNotFoundMessageFor(
            Book book, UserResource.User user, ZonedDateTime lateThreshold) {
        return "Did not find expected overdue notification. Was expecting one for ISBN \""
                + book.isbn()
                + "\", user ID \""
                + user.id()
                + "\" and late date \""
                + lateThreshold.format(DATE_TIME_FORMATTER)
                + "\".";
    }

    private static Properties createConsumerProperties() {
        var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    private static class OverdueNotificationMessage {
        private String bookIsbn;
        private String userId;
        private String lateAsOf;

        public void setBookIsbn(String bookIsbn) {
            this.bookIsbn = bookIsbn;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setLateAsOf(String lateAsOf) {
            this.lateAsOf = lateAsOf;
        }

        public boolean isFor(Book book, UserResource.User user, ZonedDateTime lateThreshold) {
            return bookIsbn.equals(book.isbn())
                    && userId.equals(user.id())
                    && lateAsOf.equals(lateThreshold.format(DATE_TIME_FORMATTER));
        }
    }
}
