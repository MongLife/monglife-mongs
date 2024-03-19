package com.mongs.core.vo.mqtt;

import com.mongs.core.enums.mqtt.PublishCode;
import lombok.Builder;

@Builder
public record BasicPublish<T>(
        PublishCode code,
        T data
) {
}
