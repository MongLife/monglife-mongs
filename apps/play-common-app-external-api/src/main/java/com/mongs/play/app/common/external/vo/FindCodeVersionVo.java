package com.mongs.play.app.common.external.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindCodeVersionVo(
        String newestBuildVersion,
        LocalDateTime createdAt,
        Boolean mustUpdateApp,
        Boolean mustUpdateCode
) {
}
