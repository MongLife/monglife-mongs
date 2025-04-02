package com.mongs.play.domain.member.vo;

import com.mongs.play.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberVo(
        Long accountId,
        Integer slotCount,
        Integer starPoint
) {
    public static MemberVo of(Member member) {
        return MemberVo.builder()
                .accountId(member.getAccountId())
                .slotCount(member.getSlotCount())
                .starPoint(member.getStarPoint())
                .build();
    }
}
