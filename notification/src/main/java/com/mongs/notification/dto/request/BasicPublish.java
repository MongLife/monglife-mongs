package com.mongs.notification.dto.request;

import com.mongs.notification.code.PublishCode;
import lombok.Builder;

@Builder
public record BasicPublish<T>(
        PublishCode code,
        T data
) {
}
