package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.MongFeedLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MongFeedLogRepository extends JpaRepository<MongFeedLog, Long> {

    List<MongFeedLog> findByMongId(Long mongId);

    Optional<MongFeedLog> findByMongIdAndCode(Long mongId, String code);
}
