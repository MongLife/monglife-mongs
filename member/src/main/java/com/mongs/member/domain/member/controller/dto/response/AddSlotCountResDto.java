package com.mongs.member.domain.member.controller.dto.response;

import lombok.Builder;

@Builder
public record AddSlotCountResDto(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
