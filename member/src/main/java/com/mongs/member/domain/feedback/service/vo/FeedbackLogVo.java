package com.mongs.member.domain.feedback.service.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FeedbackLogVo(
        String location,
        String message,
        LocalDateTime createdAt
) {
}
