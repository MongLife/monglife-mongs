package com.mongs.core.vo.member.member;

import lombok.Builder;

@Builder
public record ModifySlotCountVo(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
