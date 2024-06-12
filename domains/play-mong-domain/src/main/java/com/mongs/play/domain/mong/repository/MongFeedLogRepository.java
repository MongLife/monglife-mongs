package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.MongFeedLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongFeedLogRepository extends JpaRepository<MongFeedLog, Long> {
    @Query("SELECT ml FROM MongFeedLog ml WHERE ml.mongId = :mongId")
    List<MongFeedLog> findByMongId(@Param("mongId") Long mongId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ml FROM MongFeedLog ml WHERE ml.mongId = :mongId AND ml.code = :code")
    Optional<MongFeedLog> findByMongIdAndCodeWithLock(@Param("mongId") Long mongId, @Param("code") String code);
}
