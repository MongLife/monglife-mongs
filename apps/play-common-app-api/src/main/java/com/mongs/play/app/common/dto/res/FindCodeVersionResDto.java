package com.mongs.play.app.common.dto.res;

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
