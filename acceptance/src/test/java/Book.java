import java.util.Random;

public record Book(String isbn, int checkoutTimeInDays) {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_ISBN_LENGTH = 18;
    private static final int DEFAULT_CHECKOUT_TIME_IN_DAYS = 60;

    public Book() {
        this(randomString(), 30);
    }

    public Book(String isbn) {
        this(isbn, DEFAULT_CHECKOUT_TIME_IN_DAYS);
    }

    public Book(int checkoutTimeInDays) {
        this(randomString(), checkoutTimeInDays);
    }

    // Thank you, Suresh Atta! https://stackoverflow.com/a/20536597/6741328
    private static String randomString() {
        StringBuilder s = new StringBuilder();
        Random rnd = new Random();
        while (s.length() < RANDOM_ISBN_LENGTH) {
            int index = (int) (rnd.nextFloat() * CHARACTERS.length());
            s.append(CHARACTERS.charAt(index));
        }
        return s.toString();
    }
}
