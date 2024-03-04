package com.mongs.member.domain.feedback.service;

import com.mongs.member.domain.feedback.dto.response.FindFeedbackResDto;
import com.mongs.member.domain.feedback.dto.response.RegisterFeedbackResDto;
import com.mongs.member.domain.feedback.dto.response.SolveFeedbackResDto;
import com.mongs.member.domain.feedback.entity.Feedback;
import com.mongs.member.domain.feedback.entity.FeedbackLog;
import com.mongs.member.domain.feedback.exception.FeedbackErrorCode;
import com.mongs.member.domain.feedback.exception.NotFoundFeedbackException;
import com.mongs.member.domain.feedback.repository.FeedbackRepository;
import com.mongs.member.domain.feedback.vo.FeedbackLogVO;
import com.mongs.member.domain.feedback.vo.FeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public List<FindFeedbackResDto> findFeedbackByAccountId(Long accountId) {
        List<Feedback> feedbackList = feedbackRepository.findByAccountId(accountId);

        return FindFeedbackResDto.toList(feedbackList);
    }

    @Transactional
    public RegisterFeedbackResDto registerFeedback(
            Long accountId,
            String deviceId,
            FeedbackVO feedbackVO,
            List<FeedbackLogVO> feedbackLogVOList
    ) {
        Feedback feedback = Feedback.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .code(feedbackVO.code())
                .title(feedbackVO.title())
                .content(feedbackVO.content())
                .build();

        feedbackLogVOList.forEach(feedbackLogVO -> {
            feedback.addFeedbackLog(FeedbackLog.builder()
                        .location(feedbackLogVO.location())
                        .message(feedbackLogVO.message())
                        .createdAt(feedbackLogVO.createdAt())
                        .build());
        });

        Feedback saveFeedback = feedbackRepository.save(feedback);

        return RegisterFeedbackResDto.of(saveFeedback);
    }

    @Transactional
    public SolveFeedbackResDto solveFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundFeedbackException(FeedbackErrorCode.NOT_FOUND_FEEDBACK));

        Feedback modifyFeedback =
                feedbackRepository.save(feedback.toBuilder()
                .isSolved(true)
                .build());

        return SolveFeedbackResDto.of(modifyFeedback);
    }
}
