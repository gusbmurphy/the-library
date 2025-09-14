package org.example.repository;

import java.util.Optional;
import org.example.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findBookByIsbn(String isbn);
}
