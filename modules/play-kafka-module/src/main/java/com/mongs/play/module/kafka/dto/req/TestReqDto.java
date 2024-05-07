package com.mongs.play.module.kafka.dto.req;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TestReqDto(
        String message,
        Integer count,
        LocalDateTime createdAt
) {
}
