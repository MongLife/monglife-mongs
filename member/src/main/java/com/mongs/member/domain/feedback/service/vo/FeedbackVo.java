package com.mongs.member.domain.feedback.service.vo;

import lombok.Builder;

@Builder
public record FeedbackVo(
        String code,
        String title,
        String content
) {
}
