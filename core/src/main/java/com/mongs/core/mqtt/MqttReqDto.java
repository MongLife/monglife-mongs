package com.mongs.core.mqtt;

import lombok.Builder;

@Builder
public record MqttReqDto<T>(
        Long accountId,
        T data
) {
}
