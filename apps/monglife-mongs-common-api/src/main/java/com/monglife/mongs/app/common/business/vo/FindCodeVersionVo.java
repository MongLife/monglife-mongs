package com.monglife.mongs.app.common.business.vo;

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
