package com.mongs.common.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindVersionVo(
        String newestBuildVersion,
        LocalDateTime createdAt,
        Boolean mustUpdateApp,
        Boolean mustUpdateCode
) {
}
