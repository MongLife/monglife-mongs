package com.mongs.play.app.battle.worker.vo;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Builder
public class BattleRoomVo {
    @Builder.Default
    private String roomId = UUID.randomUUID().toString().replaceAll("-", "");
    @Builder.Default
    private Integer round = 0;
    @Builder.Default
    private Map<Long, BattlePlayerVo> playerVoMap = new ConcurrentHashMap<>();
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
