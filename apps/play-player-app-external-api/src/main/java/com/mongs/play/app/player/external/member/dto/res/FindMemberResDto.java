package com.mongs.play.app.player.external.member.dto.res;

import lombok.Builder;

@Builder
public record FindMemberResDto(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
