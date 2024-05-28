package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.MongFeedLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongFeedLogRepository extends JpaRepository<MongFeedLog, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<MongFeedLog> findByMongId(Long mongId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MongFeedLog> findByMongIdAndCode(Long mongId, String code);
}
