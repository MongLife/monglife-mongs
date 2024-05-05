package com.mongs.play.domain.feedback.service;

import com.mongs.play.core.error.domain.FeedbackErrorCode;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.feedback.entity.Feedback;
import com.mongs.play.domain.feedback.entity.FeedbackLog;
import com.mongs.play.domain.feedback.repository.FeedbackLogRepository;
import com.mongs.play.domain.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackLogRepository feedbackLogRepository;

    public List<Feedback> getFeedback(Long accountId) {
        return feedbackRepository.findByAccountId(accountId);
    }

    public Feedback addFeedback(Feedback feedback, List<FeedbackLog> feedbackLogList) {

        List<String> feedbackLogIdList = feedbackLogList.stream()
                .map(FeedbackLog::getId)
                .toList();

        List<String> existFeedbackLogIdList = feedbackLogRepository.findByIdIn(feedbackLogIdList).stream()
                .map(FeedbackLog::getId)
                .toList();

        feedbackLogList.forEach(feedbackLog -> {
            if (!existFeedbackLogIdList.contains(feedbackLog.getId())) {
                feedbackLogRepository.save(feedbackLog);
            }
        });

        return feedbackRepository.save(feedback);
    }

    public Feedback modifyIsSolved(Long feedbackId, Boolean isSolved) throws NotFoundException {

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException(FeedbackErrorCode.NOT_FOUND_FEEDBACK));

        return feedbackRepository.save(feedback.toBuilder()
                .isSolved(isSolved)
                .build());
    }
}
