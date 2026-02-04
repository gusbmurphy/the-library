package fun.gusmurphy.library.springboothex.adapter.bookarrivalkafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import fun.gusmurphy.library.springboothex.domain.port.driving.ReceivesBooks;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaBookAdapter {

    private final ReceivesBooks bookReceiver;
    private final DeserializesBookMessages deserializer;

    public KafkaBookAdapter(ReceivesBooks bookReceiver, DeserializesBookMessages deserializer) {
        this.bookReceiver = bookReceiver;
        this.deserializer = deserializer;
    }

    @KafkaListener(id = "hex-application", topics = "book-arrivals")
    public void onBookArrivalMessage(String message) {
        try {
            var deserializedBook = deserializer.deserializeMessageJsonString(message);
            bookReceiver.receiveBook(deserializedBook);
        } catch (JsonProcessingException e) {
            // TODO: What should actually happen here?
            throw new RuntimeException(e);
        }
    }
}
