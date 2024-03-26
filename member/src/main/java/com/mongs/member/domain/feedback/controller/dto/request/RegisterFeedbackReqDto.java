package com.mongs.member.domain.feedback.controller.dto.request;

import com.mongs.member.domain.feedback.service.vo.FeedbackLogVo;
import com.mongs.member.domain.feedback.service.vo.FeedbackVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterFeedbackReqDto(
        @NotNull
        FeedbackVo feedback,
        List<FeedbackLogVo> feedbackLogList
) {
}
