package com.monglife.mongs.domain.member.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberStepVo {

    private final Integer totalWalkingCount;

    private final Integer consumeWalkingCount;

    private final Integer walkingCount;

    @Builder
    public MemberStepVo(Integer totalWalkingCount, Integer consumeWalkingCount, Integer walkingCount) {
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
