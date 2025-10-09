package fun.gusmurphy.library.springboothex.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import fun.gusmurphy.library.springboothex.application.ReceivesBooks;
import org.springframework.kafka.annotation.KafkaListener;

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
