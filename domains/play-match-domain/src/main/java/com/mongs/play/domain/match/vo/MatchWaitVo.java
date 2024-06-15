package com.mongs.play.domain.match.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record MatchWaitVo(
        String playerId,
        Long mongId,
        String deviceId
) {
}
