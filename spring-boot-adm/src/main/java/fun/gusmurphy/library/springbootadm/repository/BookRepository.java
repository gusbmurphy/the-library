package fun.gusmurphy.library.springbootadm.repository;

import java.util.Optional;
import fun.gusmurphy.library.springbootadm.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findBookByIsbn(String isbn);
}
