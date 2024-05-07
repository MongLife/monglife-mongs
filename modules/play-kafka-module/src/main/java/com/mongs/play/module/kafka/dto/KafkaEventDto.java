package com.mongs.play.module.kafka.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record KafkaEventDto<T>(
        String id,
        T data
) {
}

