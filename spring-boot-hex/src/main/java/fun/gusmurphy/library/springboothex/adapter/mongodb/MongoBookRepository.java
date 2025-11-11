package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.domain.Book;
import fun.gusmurphy.library.springboothex.domain.Isbn;
import fun.gusmurphy.library.springboothex.port.driven.BookRepository;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoBookRepository implements BookRepository {

    private final MongoTemplate template;

    public MongoBookRepository(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public void saveBook(Book book) {
        var document = BookDocument.from(book);
        template.save(document);
    }

    @Override
    public Optional<Book> findByIsbn(Isbn isbn) {
        return Optional.ofNullable(template.findById(isbn.toString(), BookDocument.class))
                .map(BookDocument::toDomain);
    }

    @Override
    public Collection<Book> findAllDueAtOrBefore(ZonedDateTime time) {
        throw new UnsupportedOperationException();
    }
}
