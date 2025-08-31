import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    private static final Book UNKNOWN_BOOK = new Book("WE_DONT_KNOW_THIS_ONE");

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
        sendKafkaMessageForArrivalOf(book);
        return book;
    }

    /** Returns a book that does not exist in the library system. */
    public static Book unknownBook() {
        return UNKNOWN_BOOK;
    }

    private KafkaProducer<String, String> createKafkaProducer() {
        Properties props = createKafkaProducerProperties();

        try (var adminClient = AdminClient.create(props)) {
            if (newBookTopicDoesNotExist(adminClient)) {
                createNewBookTopic(adminClient);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new KafkaProducer<>(props);
    }

    private static Properties createKafkaProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "acceptance-harness");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    private boolean newBookTopicDoesNotExist(AdminClient adminClient)
            throws ExecutionException, InterruptedException, TimeoutException {
        ListTopicsResult topics = adminClient.listTopics();
        var listings = topics.listings().get(1, TimeUnit.SECONDS);
        return listings.stream().noneMatch(listing -> listing.name().equals(NEW_BOOK_TOPIC_NAME));
    }

    private static void createNewBookTopic(AdminClient adminClient)
            throws InterruptedException, ExecutionException, TimeoutException {
        var topic = new NewTopic(NEW_BOOK_TOPIC_NAME, 3, (short) 1);
        var createTopicsResult = adminClient.createTopics(Collections.singleton(topic));
        createTopicsResult.config(NEW_BOOK_TOPIC_NAME).get(1, TimeUnit.SECONDS);
    }

    private void sendKafkaMessageForArrivalOf(Book book)
            throws InterruptedException, ExecutionException {
        var arrivalMessage = createArrivalMessageFor(book);
        Future<RecordMetadata> future =
                kafkaProducer.send(
                        new ProducerRecord<>(NEW_BOOK_TOPIC_NAME, book.isbn(), arrivalMessage));
        future.get();
    }

    private static String createArrivalMessageFor(Book book) {
        return """
                    {
                        "isbn": "%s"
                    }
                """
                .formatted(book.isbn());
    }
}
