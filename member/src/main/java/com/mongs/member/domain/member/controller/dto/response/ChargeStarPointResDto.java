package com.mongs.member.domain.member.controller.dto.response;

import lombok.Builder;

@Builder
public record ChargeStarPointResDto(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
