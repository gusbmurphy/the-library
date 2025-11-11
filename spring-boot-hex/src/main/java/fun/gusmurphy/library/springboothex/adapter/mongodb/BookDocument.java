package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.domain.UserId;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Book")
record BookDocument(
        @Id String isbn,
        int checkoutTimeInDays,
        ZonedDateTime checkedOutAt,
        ZonedDateTime dueBackAt,
        String checkedOutByUserId) {
    static BookDocument from(Book book) {
        return new BookDocument(
                book.isbn().toString(),
                book.checkoutTimeInDays(),
                book.checkedOutAt(),
                book.checkedOutAt() != null
                        ? book.checkedOutAt().plusDays(book.checkoutTimeInDays())
                        : null,
                book.checkedOutBy() != null ? book.checkedOutBy().toString() : null);
    }

    Book toDomain() {
        return new Book(
                Isbn.fromString(isbn),
                checkoutTimeInDays,
                checkedOutAt,
                checkedOutByUserId != null ? UserId.fromString(checkedOutByUserId) : null);
    }
}
