package fun.gusmurphy.library.springbootadm.repository;

import fun.gusmurphy.library.springbootadm.domain.CheckoutRecord;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRecordRepository extends JpaRepository<CheckoutRecord, UUID> {

    @Query("select cr from CheckoutRecord cr where cr.book.isbn = :isbn")
    List<CheckoutRecord> findByBookIsbn(@Param("isbn") String isbn);

    @Query("select count(cr) from CheckoutRecord cr where cr.userId = :userId")
    long countByUserId(@Param("userId") String userId);
}
