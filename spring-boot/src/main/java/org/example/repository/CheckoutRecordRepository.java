package org.example.repository;

import java.util.List;
import java.util.UUID;
import org.example.domain.CheckoutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRecordRepository extends JpaRepository<CheckoutRecord, UUID> {

    @Query("select cr from CheckoutRecord cr where cr.book.isbn = :isbn")
    List<CheckoutRecord> findByBookIsbn(@Param("isbn") String isbn);
}
