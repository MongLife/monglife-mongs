package com.mongs.member.domain.member.controller.dto.response;

import com.mongs.member.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record RegisterMemberResDto(
        Long accountId,
        Integer maxSlot,
        Integer starPoint
) {
}
