package com.mongs.play.app.battle.worker.vo;

import lombok.Builder;

@Builder
public record RemoveMatchWaitVo(
        Long mongId,
        String deviceId
) {
}
