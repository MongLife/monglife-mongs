package com.mongs.play.client.publisher.event.dto;

import com.mongs.play.client.publisher.event.code.PublishEventCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicPublishEventDto<T> {
    private PublishEventCode code;
    private T data;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
