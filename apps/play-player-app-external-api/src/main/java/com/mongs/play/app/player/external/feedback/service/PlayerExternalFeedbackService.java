package com.mongs.play.app.player.external.feedback.service;

import com.mongs.play.app.player.external.feedback.vo.FeedbackLogVo;
import com.mongs.play.app.player.external.feedback.vo.FeedbackVo;
import com.mongs.play.app.player.external.feedback.vo.RegisterFeedbackVo;
import com.mongs.play.domain.feedback.entity.Feedback;
import com.mongs.play.domain.feedback.entity.FeedbackLog;
import com.mongs.play.domain.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerExternalFeedbackService {

    private final FeedbackService feedbackService;

    @Transactional
    public RegisterFeedbackVo registerFeedback(Long accountId, String deviceId, FeedbackVo feedbackVo, List<FeedbackLogVo> feedbackLogVoList) {

        Feedback feedback = Feedback.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .message(feedbackVo.message())
                .build();

        List<FeedbackLog> feedbackLogList = feedbackLogVoList.stream()
                .map(feedbackLogVo -> FeedbackLog.builder()
                        .id(feedbackLogVo.feedbackLogId())
                        .location(feedbackLogVo.location())
                        .message(feedbackLogVo.message())
                        .createdAt(feedbackLogVo.createdAt())
                        .build())
                .toList();

        Feedback saveFeedback = feedbackService.addFeedback(feedback, feedbackLogList);

        return RegisterFeedbackVo.of(saveFeedback);
    }
}
