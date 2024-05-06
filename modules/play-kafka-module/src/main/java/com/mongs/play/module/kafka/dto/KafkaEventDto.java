package com.mongs.play.module.kafka.dto;

public record KafkaEventDto<T>(String id, T data) {
}
