package com.mongs.play.domain.account.repository;

import com.mongs.play.domain.account.entity.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AccountLogRepository extends JpaRepository<AccountLog, Long> {
    Optional<AccountLog> findByAccountIdAndDeviceIdAndLoginAt(Long accountId, String deviceId, LocalDate createdAt);
}
