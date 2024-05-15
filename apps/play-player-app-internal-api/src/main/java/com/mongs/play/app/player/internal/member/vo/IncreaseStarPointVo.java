package com.mongs.play.app.player.internal.member.vo;

import lombok.Builder;

@Builder
public record IncreaseStarPointVo(
        Long accountId,
        Integer starPoint,
        Integer addStarPoint
) {
}
