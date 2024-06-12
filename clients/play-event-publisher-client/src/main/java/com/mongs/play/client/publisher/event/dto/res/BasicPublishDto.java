package com.mongs.play.client.publisher.event.dto.res;

import com.mongs.play.client.publisher.event.code.PublishCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicPublishDto<T> {
    private PublishCode code;
    private T data;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
