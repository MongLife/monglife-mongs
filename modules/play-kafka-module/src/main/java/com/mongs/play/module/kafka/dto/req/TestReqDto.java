package com.mongs.play.module.kafka.dto.req;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record TestReqDto(
        String message,
        Integer count,
        LocalDateTime createdAt
) {
}
