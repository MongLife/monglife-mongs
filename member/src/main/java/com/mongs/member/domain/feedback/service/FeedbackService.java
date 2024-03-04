package com.mongs.member.domain.feedback.service;

import com.mongs.member.domain.feedback.dto.response.FindFeedbackResDto;
import com.mongs.member.domain.feedback.dto.response.RegisterFeedbackResDto;
import com.mongs.member.domain.feedback.dto.response.SolveFeedbackResDto;
import com.mongs.member.domain.feedback.entity.Feedback;
import com.mongs.member.domain.feedback.repository.FeedbackRepository;
import com.mongs.member.domain.feedback.vo.FeedbackLogVO;
import com.mongs.member.domain.feedback.vo.FeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public List<FindFeedbackResDto> findFeedbackByAccountId(Long accountId) {
        List<Feedback> feedbackList = feedbackRepository.findByAccountId(accountId);

        return FindFeedbackResDto.toList(feedbackList);
    }

    public RegisterFeedbackResDto registerFeedback(FeedbackVO feedbackVO, List<FeedbackLogVO> feedbackLogVOList) {
        return null;
    }

    public SolveFeedbackResDto solveFeedback(Long feedbackId) {
        return null;
    }
}
