package com.mongs.play.domain.feedback.repository;

import com.mongs.play.domain.feedback.entity.Feedback;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Feedback> findByAccountId(Long accountId);
}
