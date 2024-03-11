package com.mongs.member.domain.feedback.dto.request;

import com.mongs.member.domain.feedback.vo.FeedbackLogVO;
import com.mongs.member.domain.feedback.vo.FeedbackVO;

import java.util.List;

public record RegisterFeedbackReqDto(
        FeedbackVO feedback,
        List<FeedbackLogVO> feedbackLogList
) {
}
