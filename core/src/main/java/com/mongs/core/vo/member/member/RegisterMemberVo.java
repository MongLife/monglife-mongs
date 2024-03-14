package com.mongs.core.vo.member.member;

import lombok.Builder;

@Builder
public record RegisterMemberVo(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
