package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.MongFeedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MongFeedLogRepository extends JpaRepository<MongFeedLog, Long> {
    @Query("SELECT L FROM MongFeedLog L WHERE L.mongId = :mongId ORDER BY L.createdAt DESC")
    List<MongFeedLog> findByMongId(Long mongId);
}
