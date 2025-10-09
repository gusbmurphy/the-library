package fun.gusmurphy.library.springboothex.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.gusmurphy.library.springboothex.domain.Book;

public class ObjectMapperBookMessageDeserializer implements DeserializesBookMessages {

    private final ObjectMapper mapper;

    public ObjectMapperBookMessageDeserializer() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public Book deserializeMessageJsonString(String message) throws JsonProcessingException {
        var parsedJson = mapper.readValue(message, BookArrivalMessageJson.class);
        return new Book(parsedJson.getIsbn(), parsedJson.getCheckoutTimeInDays());
    }
}
