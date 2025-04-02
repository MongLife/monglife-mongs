package com.mongs.play.app.player.external.member.dto.res;

import lombok.Builder;

@Builder
public record BuySlotResDto(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
