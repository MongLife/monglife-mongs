package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record MqttReqDto<T>(
        Long accountId,
        T data
) {
}
