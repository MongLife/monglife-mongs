package com.mongs.play.app.player.external.feedback.dto.req;

import com.mongs.play.app.player.external.feedback.vo.FeedbackLogVo;
import com.mongs.play.app.player.external.feedback.vo.FeedbackVo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterFeedbackReqDto(
        @NotNull
        FeedbackVo feedback,
        List<FeedbackLogVo> feedbackLogList
) {
}
