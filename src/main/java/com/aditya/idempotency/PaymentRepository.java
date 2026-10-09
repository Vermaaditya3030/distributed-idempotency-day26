package com.aditya.idempotency;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PaymentRepository extends JpaRepository<PaymentRecord,UUID>{ Optional<PaymentRecord> findByIdempotencyKey(String idempotencyKey); }
