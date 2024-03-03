package com.mongs.member.domain.member.dto.response;

import com.mongs.member.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record RemoveMemberResDto(
        Long memberId,
        Integer maxSlot,
        Integer startPoint
) {
    public static RemoveMemberResDto of(Member member) {
        return RemoveMemberResDto.builder()
                .memberId(member.getAccountId())
                .maxSlot(member.getMaxSlot())
                .startPoint(member.getStartPoint())
                .build();
    }
}
