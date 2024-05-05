package com.mongs.play.app.player.external.member.dto.res;

import lombok.Builder;

@Builder
public record ExchangePayPointResDto(
        Long accountId,
        Integer starPoint,
        Long mongId,
        Integer payPoint
) {
}
