package com.mongs.play.app.player.external.member.vo;

import lombok.Builder;

@Builder
public record ChargeStarPointVo(
        Long accountId,
        Integer starPoint
) {
}
