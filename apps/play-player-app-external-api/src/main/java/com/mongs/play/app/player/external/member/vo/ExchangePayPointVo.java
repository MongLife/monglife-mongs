package com.mongs.play.app.player.external.member.vo;

import lombok.Builder;

@Builder
public record ExchangePayPointVo(
        Long accountId,
        Long mongId,
        Integer subStarPoint,
        Integer addPayPoint
) {
}
