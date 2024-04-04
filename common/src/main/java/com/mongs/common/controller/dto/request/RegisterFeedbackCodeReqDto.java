package com.mongs.common.controller.dto.request;

import lombok.Builder;

@Builder
public record RegisterFeedbackCodeReqDto(
        String code,
        String groupCode,
        String message,
        String buildVersion
) {
}
