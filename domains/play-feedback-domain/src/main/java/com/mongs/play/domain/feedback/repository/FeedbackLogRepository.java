package com.mongs.play.domain.feedback.repository;

import com.mongs.play.domain.feedback.entity.FeedbackLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackLogRepository extends JpaRepository<FeedbackLog, Long> {
    List<FeedbackLog> findByIdIn(List<String> feedbackLogIdList);
}
