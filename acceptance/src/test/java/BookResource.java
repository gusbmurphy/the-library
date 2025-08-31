import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class BookResource {

    private final KafkaProducer<String, String> kafkaProducer;
    private static final String NEW_BOOK_TOPIC_NAME = "book-arrivals";

    public BookResource() {
        kafkaProducer = createKafkaProducer();
    }

    /**
     * Creates a new book in the library system by sending a message to the Kafka topic.
     *
     * @return the new book
     */
    public Book newBookArrives() throws ExecutionException, InterruptedException {
        var book = new Book();
        var arrivalMessage =
                """
                    {
                        "isbn": "%s"
                    }
                """
                        .formatted(book.isbn());
        Future<RecordMetadata> future =
                kafkaProducer.send(
                        new ProducerRecord<>(NEW_BOOK_TOPIC_NAME, book.isbn(), arrivalMessage));
        future.get();
        return book;
    }

    private static KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "acceptance-harness");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (var adminClient = AdminClient.create(props)) {
            ListTopicsResult topics = adminClient.listTopics();
            var listings = topics.listings().get(1, TimeUnit.SECONDS);
            if (listings.stream()
                    .noneMatch(listing -> listing.name().equals(NEW_BOOK_TOPIC_NAME))) {
                var topic = new NewTopic(NEW_BOOK_TOPIC_NAME, 3, (short) 1);
                var createTopicsResult = adminClient.createTopics(Collections.singleton(topic));
                createTopicsResult.config(NEW_BOOK_TOPIC_NAME).get(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new KafkaProducer<>(props);
    }

    public Book unknownBook() {
        return new Book("WE_DONT_KNOW_THIS_ONE");
    }
}
