package com.mongs.member.domain.member.dto.response;

import com.mongs.member.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record FindMemberResDto(
        Integer maxSlot,
        Integer startPoint
) {
    public static FindMemberResDto of(Member member) {
        return FindMemberResDto.builder()
                .maxSlot(member.getMaxSlot())
                .startPoint(member.getStartPoint())
                .build();
    }
}
