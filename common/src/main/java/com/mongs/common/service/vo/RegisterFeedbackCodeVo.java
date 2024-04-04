package com.mongs.common.service.vo;

import lombok.Builder;

@Builder
public record RegisterFeedbackCodeVo(
        String code,
        String groupCode,
        String message
) {
}
