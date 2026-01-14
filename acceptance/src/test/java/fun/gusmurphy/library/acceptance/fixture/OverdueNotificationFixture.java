package fun.gusmurphy.library.acceptance.fixture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;

public class OverdueNotificationFixture {

    private final KafkaConsumer<String, String> kafkaConsumer;
    private static final String OVERDUE_TOPIC_NAME = "overdue-notifications";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final Duration KAFKA_POLLING_DURATION = Duration.ofMillis(200);
    private static final int MAX_EXISTENCE_RETRIES = 5;
    private static final int MAX_ABSENCE_RETRIES = 3;

    public OverdueNotificationFixture() {
        var props = createConsumerProperties();
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(List.of(OVERDUE_TOPIC_NAME));
    }

    public void oneExistsFor(Book book, UserFixture.User user, ZonedDateTime lateThreshold)
            throws JsonProcessingException {
        assertNotificationExistsForAndRetry(book, user, lateThreshold, 0);
    }

    public void noneExistFor(Book book, UserFixture.User user, ZonedDateTime lateThreshold)
            throws JsonProcessingException {
        assertNoneExistForAndRetry(book, user, lateThreshold, 0);
    }

    private void assertNotificationExistsForAndRetry(
            Book book, UserFixture.User user, ZonedDateTime lateThreshold, int retryCount)
            throws JsonProcessingException {
        ConsumerRecords<String, String> records = pollForAllRecords();
        var actualMessages = new ArrayList<OverdueNotificationMessage>();

        for (var record : records) {
            var message = MAPPER.readValue(record.value(), OverdueNotificationMessage.class);
            actualMessages.add(message);
            if (message.isFor(book, user, lateThreshold)) {
                return;
            }
        }

        if (retryCount < MAX_EXISTENCE_RETRIES) {
            assertNotificationExistsForAndRetry(book, user, lateThreshold, retryCount + 1);
        } else {
            Assertions.fail(
                    notificationNotFoundMessageFor(book, user, lateThreshold, actualMessages));
        }
    }

    private void assertNoneExistForAndRetry(
            Book book, UserFixture.User user, ZonedDateTime lateThreshold, int retryCount)
            throws JsonProcessingException {
        ConsumerRecords<String, String> records = pollForAllRecords();

        for (var record : records) {
            var message = MAPPER.readValue(record.value(), OverdueNotificationMessage.class);
            if (message.isFor(book, user, lateThreshold)) {
                Assertions.fail(
                        notificationFoundWhenNotExpectedMessageFor(book, user, lateThreshold));
            }
        }

        if (retryCount < MAX_ABSENCE_RETRIES) {
            assertNoneExistForAndRetry(book, user, lateThreshold, retryCount + 1);
        }
    }

    private ConsumerRecords<String, String> pollForAllRecords() {
        kafkaConsumer.seekToBeginning(kafkaConsumer.assignment());
        return kafkaConsumer.poll(KAFKA_POLLING_DURATION);
    }

    private String notificationNotFoundMessageFor(
            Book book,
            UserFixture.User user,
            ZonedDateTime lateThreshold,
            List<OverdueNotificationMessage> actualMessages) {
        var message =
                "Did not find expected overdue notification.\n"
                        + "Expected: ISBN=\""
                        + book.isbn()
                        + "\", userId=\""
                        + user.id()
                        + "\", lateAsOf=\""
                        + lateThreshold.format(DATE_TIME_FORMATTER)
                        + "\"\n";

        if (actualMessages.isEmpty()) {
            message += "Actual: No messages found in Kafka topic.";
        } else {
            message += "Actual messages found (" + actualMessages.size() + "):\n";
            for (int i = 0; i < actualMessages.size(); i++) {
                var msg = actualMessages.get(i);
                message +=
                        "  "
                                + (i + 1)
                                + ". ISBN=\""
                                + msg.bookIsbn
                                + "\", userId=\""
                                + msg.userId
                                + "\", lateAsOf=\""
                                + msg.lateAsOf
                                + "\"\n";
            }
        }

        return message;
    }

    private String notificationFoundWhenNotExpectedMessageFor(
            Book book, UserFixture.User user, ZonedDateTime lateThreshold) {
        return "Found an overdue notification when one was not expected for ISBN \""
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
        String bookIsbn;
        String userId;
        String lateAsOf;

        public void setBookIsbn(String bookIsbn) {
            this.bookIsbn = bookIsbn;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setLateAsOf(String lateAsOf) {
            this.lateAsOf = lateAsOf;
        }

        public boolean isFor(Book book, UserFixture.User user, ZonedDateTime lateThreshold) {
            return bookIsbn.equals(book.isbn())
                    && userId.equals(user.id())
                    && lateAsOfAsZonedDateTime().isEqual(lateThreshold);
        }

        private ZonedDateTime lateAsOfAsZonedDateTime() {
            return ZonedDateTime.from(DATE_TIME_FORMATTER.parse(lateAsOf));
        }
    }
}
