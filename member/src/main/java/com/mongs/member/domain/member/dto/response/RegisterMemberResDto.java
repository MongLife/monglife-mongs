package com.mongs.member.domain.member.dto.response;

import com.mongs.member.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record RegisterMemberResDto(
        Long memberId,
        Integer maxSlot,
        Integer startPoint
) {
    public static RegisterMemberResDto of(Member member) {
        return RegisterMemberResDto.builder()
                .memberId(member.getAccountId())
                .maxSlot(member.getMaxSlot())
                .startPoint(member.getStartPoint())
                .build();
    }
}
