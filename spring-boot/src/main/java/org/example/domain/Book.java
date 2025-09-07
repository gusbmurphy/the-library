package org.example.domain;

public class Book {
    private String isbn;
    private int checkoutTimeInDays;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCheckoutTimeInDays() {
        return checkoutTimeInDays;
    }

    public void setCheckoutTimeInDays(int checkoutTimeInDays) {
        this.checkoutTimeInDays = checkoutTimeInDays;
    }
}
