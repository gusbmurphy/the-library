package fun.gusmurphy.library.springboothex.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IsbnTest {

    @Test
    void twoIsbnsFromTheSameStringAreEqual() {
        var string = "some-isbn";
        var a = Isbn.fromString(string);
        var b = Isbn.fromString(string);
        assertEquals(a, b);
    }
}
