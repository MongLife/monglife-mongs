package com.mongs.notification.controller.dto.response;

import com.mongs.notification.code.PublishCode;
import lombok.Builder;

@Builder
public record BasicPublish<T>(
        PublishCode code,
        T data
) {
}
