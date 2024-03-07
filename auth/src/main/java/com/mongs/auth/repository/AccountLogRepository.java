package com.mongs.auth.repository;

import com.mongs.auth.entity.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AccountLogRepository extends JpaRepository<AccountLog, Long> {
    Optional<AccountLog> findByAccountIdAndDeviceIdAndLoginAt(Long accountId, String deviceId, LocalDate createdAt);
    Optional<AccountLog> findTopByAccountIdAndDeviceIdOrderByLoginAt(Long accountId, String deviceId);
}
