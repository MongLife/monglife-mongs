package com.mongs.play.app.battle.worker.dto.res;

import lombok.Builder;

@Builder
public record MatchWaitResDto(
        Long mongId,
        String deviceId
) {
}
