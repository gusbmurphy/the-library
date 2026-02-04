package fun.gusmurphy.library.springboothex.application;

public class BookBuilder {

    private String isbnString;
    private int checkoutTimeInDaysInt;

    public BookBuilder() {}

    public Book build() {
        var isbn = Isbn.fromString(isbnString);
        return new Book(isbn, checkoutTimeInDaysInt);
    }

    public BookBuilder withIsbnString(String isbnString) {
        this.isbnString = isbnString;
        return this;
    }

    public BookBuilder withCheckoutTimeInDaysInt(int checkoutTimeInDays) {
        this.checkoutTimeInDaysInt = checkoutTimeInDays;
        return this;
    }
}
