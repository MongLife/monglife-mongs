package com.monglife.mongs.domain.member.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberVo {

    private final Long accountId;

    private final Integer slotCount;

    private final Integer starPoint;

    @Builder
    public MemberVo(Long accountId, Integer slotCount, Integer starPoint) {
        this.accountId = accountId;
        this.slotCount = slotCount;
        this.starPoint = starPoint;
    }
}
