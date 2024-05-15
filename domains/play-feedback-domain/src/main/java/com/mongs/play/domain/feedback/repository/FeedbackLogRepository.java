package com.mongs.play.domain.feedback.repository;

import com.mongs.play.domain.feedback.entity.FeedbackLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface FeedbackLogRepository extends JpaRepository<FeedbackLog, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<FeedbackLog> findByIdIn(List<String> feedbackLogIdList);
}
