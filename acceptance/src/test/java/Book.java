import java.nio.charset.StandardCharsets;
import java.util.Random;

public record Book(String isbn) {

    public Book() {
        this(randomString());
    }

    private static String randomString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
