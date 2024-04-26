package com.mongs.member.domain.member.service.vo;

import lombok.Builder;

@Builder
public record ExchangeStarPointVo(
        Long accountId,
        Integer starPoint
) {
}
