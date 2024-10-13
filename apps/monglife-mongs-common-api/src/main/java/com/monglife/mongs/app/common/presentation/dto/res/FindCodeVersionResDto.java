package com.monglife.mongs.app.common.presentation.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindCodeVersionResDto(
        String newestBuildVersion,
        LocalDateTime createdAt,
        Boolean updateApp,
        Boolean updateCode
) {
}
