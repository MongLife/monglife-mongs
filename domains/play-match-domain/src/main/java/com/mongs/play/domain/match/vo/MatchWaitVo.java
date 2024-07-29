package com.mongs.play.domain.match.vo;

import lombok.Builder;

import java.util.Objects;

@Builder(toBuilder = true)
public record MatchWaitVo(
        String playerId,
        long mongId,
        String deviceId,
        boolean isBot
) {
}
