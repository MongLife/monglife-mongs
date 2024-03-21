package com.mongs.member.domain.member.service.vo;

import lombok.Builder;

@Builder
public record RegisterMemberVo(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
