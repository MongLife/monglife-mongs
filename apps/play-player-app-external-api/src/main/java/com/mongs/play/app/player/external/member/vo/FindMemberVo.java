package com.mongs.play.app.player.external.member.vo;

import lombok.Builder;

@Builder
public record FindMemberVo(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
