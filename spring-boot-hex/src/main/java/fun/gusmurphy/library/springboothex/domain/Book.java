package fun.gusmurphy.library.springboothex.domain;

public class Book {

    private final Isbn isbn;
    private final int checkoutTimeInDays;

    public Book(Isbn isbn, int checkoutTimeInDays) {
        this.isbn = isbn;
        this.checkoutTimeInDays = checkoutTimeInDays;
    }

    public Isbn isbn() {
        return isbn;
    }

    public int checkoutTimeInDays() {
        return checkoutTimeInDays;
    }
}
