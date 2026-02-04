package fun.gusmurphy.library.springboothex.adapter.bookarrivalkafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import fun.gusmurphy.library.springboothex.application.domain.book.Book;

public interface DeserializesBookMessages {

    Book deserializeMessageJsonString(String message) throws JsonProcessingException;
}
