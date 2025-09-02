import java.util.Random;

public record Book(String isbn) {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_ISBN_LENGTH = 18;

    public Book() {
        this(randomString());
    }

    // Thank you Suresh Atta! https://stackoverflow.com/a/20536597/6741328
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
