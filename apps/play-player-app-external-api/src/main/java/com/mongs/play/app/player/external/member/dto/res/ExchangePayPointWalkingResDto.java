package com.mongs.play.app.player.external.member.dto.res;

import lombok.Builder;

@Builder
public record ExchangePayPointWalkingResDto(
        Long accountId,
        Long mongId,
        Integer subWalkingCount
) {
}
