package com.mongs.member.domain.member.dto.response;

import com.mongs.member.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record ModifyMemberResDto(
        Long memberId,
        Integer maxSlot,
        Integer startPoint
) {
    public static ModifyMemberResDto of(Member member) {
        return ModifyMemberResDto.builder()
                .memberId(member.getAccountId())
                .maxSlot(member.getMaxSlot())
                .startPoint(member.getStartPoint())
                .build();
    }
}
