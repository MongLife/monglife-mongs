package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.MongFeedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MongFeedLogRepository extends JpaRepository<MongFeedLog, Long> {
    @Query("SELECT MongFeedLog FROM MongFeedLog WHERE mongId = :mongId GROUP BY code ORDER BY createdAt")
    List<MongFeedLog> findByMongId(Long mongId);
}
