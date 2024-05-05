package com.mongs.play.domain.feedback.repository;

import com.mongs.play.domain.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByAccountId(Long accountId);
}
