package com.mongs.member.domain.feedback.vo;

import lombok.Builder;

@Builder
public record FeedbackVO(
        Long accountId,
        String deviceId,
        String code,
        String title,
        String content
) {
}
