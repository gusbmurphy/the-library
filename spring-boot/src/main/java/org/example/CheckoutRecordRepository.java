package org.example;

import java.util.UUID;
import org.example.domain.CheckoutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRecordRepository extends JpaRepository<CheckoutRecord, UUID> {}
