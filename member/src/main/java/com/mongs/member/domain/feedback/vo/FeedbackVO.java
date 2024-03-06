package com.mongs.member.domain.feedback.vo;

import lombok.Builder;

@Builder
public record FeedbackVO(
        String code,
        String title,
        String content
) {
}
