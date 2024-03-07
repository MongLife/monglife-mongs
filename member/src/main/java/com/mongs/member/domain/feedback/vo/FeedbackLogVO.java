package com.mongs.member.domain.feedback.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FeedbackLogVO(
        String location,
        String message,
        LocalDateTime createdAt
) {
}
