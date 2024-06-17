package com.mongs.play.app.battle.worker.vo;

import lombok.Builder;

@Builder
public record RegisterMatchWaitVo(
        Long mongId,
        String deviceId
) {
}
