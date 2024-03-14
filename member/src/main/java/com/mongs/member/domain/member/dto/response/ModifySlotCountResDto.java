package com.mongs.member.domain.member.dto.response;

import lombok.Builder;

@Builder
public record ModifySlotCountResDto(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
