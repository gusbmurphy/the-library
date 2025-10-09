package fun.gusmurphy.library.springboothex.adapter.kafka;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.doubles.BookReceiverSpy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KafkaBookAdapterTest {

    @Test
    void messagesAreCorrectlyDeserializedAndPassedToTheService() {
        DeserializesBookMessages deserializer = new ObjectMapperBookMessageDeserializer();
        BookReceiverSpy bookReceiver = new BookReceiverSpy();
        var adapter = new KafkaBookAdapter(bookReceiver, deserializer);
        var messageString = """
            {
                "isbn": "some-isbn",
                "checkoutTimeInDays": 30
            }
        """;

        adapter.onBookArrivalMessage(messageString);
        Book receivedBook = bookReceiver.lastReceivedBook();
        Assertions.assertEquals(Isbn.fromString("some-isbn"), receivedBook.isbn());
        Assertions.assertEquals(30, receivedBook.checkoutTimeInDays());
    }

}