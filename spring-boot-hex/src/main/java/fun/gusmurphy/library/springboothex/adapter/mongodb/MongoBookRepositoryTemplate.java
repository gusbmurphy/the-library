package fun.gusmurphy.library.springboothex.adapter.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoBookRepositoryTemplate extends MongoRepository<BookDocument, String> {}
