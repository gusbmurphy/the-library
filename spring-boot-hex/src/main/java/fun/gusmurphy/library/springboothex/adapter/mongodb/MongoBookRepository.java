package fun.gusmurphy.library.springboothex.adapter.mongodb;

import fun.gusmurphy.library.springboothex.application.domain.book.Book;
import fun.gusmurphy.library.springboothex.application.domain.book.Isbn;
import fun.gusmurphy.library.springboothex.application.domain.user.UserId;
import fun.gusmurphy.library.springboothex.application.port.secondary.BookRepository;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        var query = new Query();
        var dateFromZdt = Date.from(time.toInstant());
        query.addCriteria(Criteria.where("dueBackAt").lte(dateFromZdt));
        return template.find(query, BookDocument.class).stream()
                .map(BookDocument::toDomain)
                .toList();
    }

    @Override
    public Collection<Book> booksCheckedOutBy(UserId id) {
        var query = new Query();
        query.addCriteria(Criteria.where("checkedOutByUserId").is(id.toString()));
        return template.find(query, BookDocument.class).stream()
                .map(BookDocument::toDomain)
                .toList();
    }
}
