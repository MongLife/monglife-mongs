package com.mongs.member.domain.member.service.vo;

import lombok.Builder;

@Builder
public record AddSlotCountVo(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
