package fun.gusmurphy.library.springboothex.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

public class BookTest {

    private final Isbn isbn = Isbn.fromString("123");
    private final int checkOutTimeInDays = 30;
    private final Book book = new Book(isbn, checkOutTimeInDays);
    private final ZonedDateTime testTime = ZonedDateTime.now();

    @Test
    void checkingOutABook() {
        var userId = UserId.random();
        book.checkout(userId, testTime);
        assertEquals(testTime.plusDays(checkOutTimeInDays), book.dueBackBy().get());
        assertTrue(book.isCheckedOut());
    }

    @Test
    void aNonCheckedOutBookHasNoDueBackByDate() {
        assertTrue(book.dueBackBy().isEmpty());
    }
}
