package com.mongs.member.domain.feedback.repository;

import com.mongs.member.domain.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByAccountId(Long accountId);
}
