package com.mongs.play.app.player.external.feedback.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FeedbackLogVo(
        String feedbackLogId,
        String location,
        String message,
        LocalDateTime createdAt
) {
}
