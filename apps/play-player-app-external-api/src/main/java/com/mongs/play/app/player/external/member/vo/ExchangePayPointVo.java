package com.mongs.play.app.player.external.member.vo;

import lombok.Builder;

@Builder
public record ExchangePayPointVo(
        Long accountId,
        Integer starPoint,
        Long mongId,
        Integer payPoint
) {
}
