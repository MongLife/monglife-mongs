package com.mongs.core.mqtt;

import lombok.Builder;

@Builder
public record MqttReqDto<T>(
        String email,
        T data
) {
}
