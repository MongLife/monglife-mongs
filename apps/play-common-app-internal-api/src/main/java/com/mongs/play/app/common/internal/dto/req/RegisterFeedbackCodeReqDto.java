package com.mongs.play.app.common.internal.dto.req;

import lombok.Builder;

@Builder
public record RegisterFeedbackCodeReqDto(
        String code,
        String groupCode,
        String message,
        String buildVersion
) {
}
