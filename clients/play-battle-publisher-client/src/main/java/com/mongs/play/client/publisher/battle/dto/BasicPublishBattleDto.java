package com.mongs.play.client.publisher.battle.dto;

import com.mongs.play.client.publisher.battle.code.PublishBattleCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BasicPublishBattleDto<T> {
    private PublishBattleCode code;
    private T data;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
