package com.mongs.play.domain.payment.repository;

import com.mongs.play.domain.payment.entity.PaymentLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PaymentLog> findPaymentLogById(Long id);
}
