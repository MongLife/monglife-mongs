package com.mongs.play.domain.match.vo;

import lombok.Builder;

@Builder
public record MatchWaitVo(
        String playerId,
        Long mongId,
        String deviceId
) {
}
