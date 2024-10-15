package com.monglife.mongs.client.mqtt.data.dto;

import com.monglife.mongs.client.mqtt.global.code.battle.PublishBattleCode;
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
