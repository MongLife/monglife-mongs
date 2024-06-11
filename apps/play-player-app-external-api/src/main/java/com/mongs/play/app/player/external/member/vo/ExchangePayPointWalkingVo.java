package com.mongs.play.app.player.external.member.vo;

import lombok.Builder;

@Builder
public record ExchangePayPointWalkingVo(
        Long accountId,
        Long mongId,
        Integer subWalkingCount,
        Integer payPoint
) {
}
