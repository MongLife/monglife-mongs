package com.mongs.play.domain.account.repository;

import com.mongs.play.domain.account.entity.AccountLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AccountLogRepository extends JpaRepository<AccountLog, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT al FROM AccountLog al WHERE al.accountId = :accountId AND al.deviceId = :deviceId AND al.loginAt = :loginAt")
    Optional<AccountLog> findByAccountIdAndDeviceIdAndLoginAtWithLock(@Param("accountId") Long accountId, @Param("deviceId") String deviceId, @Param("loginAt") LocalDate loginAt);
}
