package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.MongLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongLogRepository extends JpaRepository<MongLog, Long> {
}
