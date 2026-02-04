package fun.gusmurphy.library.springboothex.adapter.bookarrivalkafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.gusmurphy.library.springboothex.application.Book;
import fun.gusmurphy.library.springboothex.application.BookBuilder;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperBookMessageDeserializer implements DeserializesBookMessages {

    private final ObjectMapper mapper;

    public ObjectMapperBookMessageDeserializer() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public Book deserializeMessageJsonString(String message) throws JsonProcessingException {
        var parsedJson = mapper.readValue(message, BookArrivalMessageJson.class);
        return new BookBuilder()
                .withIsbnString(parsedJson.getIsbn())
                .withCheckoutTimeInDaysInt(parsedJson.getCheckoutTimeInDays())
                .build();
    }
}
