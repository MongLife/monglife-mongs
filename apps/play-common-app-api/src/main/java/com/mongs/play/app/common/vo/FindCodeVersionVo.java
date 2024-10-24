package com.mongs.play.app.common.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindCodeVersionVo(
        String newestBuildVersion,
        LocalDateTime createdAt,
        Boolean updateApp,
        Boolean updateCode
) {
}
