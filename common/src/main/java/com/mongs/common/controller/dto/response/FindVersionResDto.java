package com.mongs.common.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FindVersionResDto(
        String newestBuildVersion,
        LocalDateTime createdAt,
        Boolean mustUpdateApp,
        Boolean mustUpdateCode
) {
}
