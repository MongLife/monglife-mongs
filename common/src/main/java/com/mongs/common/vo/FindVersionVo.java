package com.mongs.common.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindVersionVo(
        Long version,
        LocalDateTime createdAt
) {
}
