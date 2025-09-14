package fun.gusmurphy.library.springbootadm.domain;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CheckoutRecord {
    private String userId;

    @OneToOne
    @JoinColumn(name = "book_isbn")
    private Book book;

    private ZonedDateTime checkoutTime;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public CheckoutRecord(String userId, Book book, ZonedDateTime checkoutTime) {
        this.userId = userId;
        this.book = book;
        this.checkoutTime = checkoutTime;
    }
}
