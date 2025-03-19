package com.monglife.mongs.domain.device.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StepVo {

    private final Integer totalWalkingCount;

    private final Integer consumeWalkingCount;

    private final Integer walkingCount;

    @Builder
    public StepVo(Integer totalWalkingCount, Integer consumeWalkingCount, Integer walkingCount) {
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
