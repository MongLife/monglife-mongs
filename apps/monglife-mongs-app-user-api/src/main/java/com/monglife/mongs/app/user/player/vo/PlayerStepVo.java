package com.monglife.mongs.app.user.player.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlayerStepVo {

    private final Integer totalWalkingCount;

    private final Integer consumeWalkingCount;

    private final Integer walkingCount;

    @Builder
    public PlayerStepVo(Integer totalWalkingCount, Integer consumeWalkingCount, Integer walkingCount) {
        this.totalWalkingCount = totalWalkingCount;
        this.consumeWalkingCount = consumeWalkingCount;
        this.walkingCount = walkingCount;
    }
}
