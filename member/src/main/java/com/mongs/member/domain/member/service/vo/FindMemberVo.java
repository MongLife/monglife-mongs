package com.mongs.member.domain.member.service.vo;

import lombok.Builder;

@Builder
public record FindMemberVo(
        Long accountId,
        Integer maxSlot,
        Integer startPoint
) {
}
