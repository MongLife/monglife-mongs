package com.mongs.member.domain.member.service.vo;

import lombok.Builder;

@Builder
public record ModifySlotCountVo(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
