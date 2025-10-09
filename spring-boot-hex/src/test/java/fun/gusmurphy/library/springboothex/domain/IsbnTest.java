package fun.gusmurphy.library.springboothex.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IsbnTest {

    @Test
    void twoIsbnsFromTheSameStringAreEqual() {
        var string = "some-isbn";
        var a = Isbn.fromString(string);
        var b = Isbn.fromString(string);
        assertEquals(a, b);
    }

}