package com.mongs.play.client.publisher.battle.dto.res;

import com.mongs.play.client.publisher.battle.code.PublishCode;
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
