package com.monglife.mongs.app.activity.training.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainingVo {

    private final Integer rewardPayPoint;

    private final Integer score;

    private final Integer timeout;

    @Builder
    public TrainingVo(Integer rewardPayPoint, Integer score, Integer timeout) {
        this.rewardPayPoint = rewardPayPoint;
        this.score = score;
        this.timeout = timeout;
    }
}
