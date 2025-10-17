package fun.gusmurphy.library.springboothex.domain;

import java.util.Objects;

public class Isbn {

    private final String isbnString;

    private Isbn(String isbnString) {
        this.isbnString = isbnString;
    }

    public static Isbn fromString(String isbnString) {
        return new Isbn(isbnString);
    }

    @Override
    public String toString() {
        return isbnString;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Isbn isbn = (Isbn) o;
        return Objects.equals(isbnString, isbn.isbnString);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbnString);
    }
}
