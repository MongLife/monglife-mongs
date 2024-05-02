package com.mongs.play.app.common.external.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindCodeVersionResDto(
        String newestBuildVersion,
        LocalDateTime createdAt,
        Boolean mustUpdateApp,
        Boolean mustUpdateCode
) {
}
