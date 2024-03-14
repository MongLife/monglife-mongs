package com.mongs.core.vo.member.member;

import lombok.Builder;

@Builder
public record FindMemberVo(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
