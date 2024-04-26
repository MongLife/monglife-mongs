package com.mongs.member.domain.member.service.vo;

import lombok.Builder;

@Builder
public record ChargeStarPointVo(
        Long accountId,
        Integer starPoint
) {
}
