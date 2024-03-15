package com.mongs.member.domain.member.controller.dto.response;

import lombok.Builder;

@Builder
public record ModifySlotCountResDto(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
