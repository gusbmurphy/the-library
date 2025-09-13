package org.example.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class CheckoutRecord {
    private String userId;

    @ManyToOne
    private Book book;
    private ZonedDateTime checkoutTime;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public CheckoutRecord(String userId, Book book, ZonedDateTime checkoutTime) {
        this.userId = userId;
        this.book = book;
        this.checkoutTime = checkoutTime;
        id = UUID.randomUUID();
    }
}
