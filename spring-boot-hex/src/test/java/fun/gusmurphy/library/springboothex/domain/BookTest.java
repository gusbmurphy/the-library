package fun.gusmurphy.library.springboothex.domain;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void checkingOutABook() {
        var isbn = Isbn.fromString("123");
        var checkOutTimeInDays = 30;
        var book = new Book(isbn, checkOutTimeInDays);

        var checkoutTime = ZonedDateTime.now();
        var userId = UserId.random();

        book.checkout(userId, checkoutTime);
        assertEquals(checkoutTime.plusDays(checkOutTimeInDays), book.dueBackBy().get());
    }

}
