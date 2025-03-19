package com.monglife.mongs.app.activity.training.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainingEndVo {

    private final Boolean isSuccess;

    private final Integer rewardPayPoint;

    private final Integer score;

    @Builder
    public TrainingEndVo(Boolean isSuccess, Integer rewardPayPoint, Integer score) {
        this.isSuccess = isSuccess;
        this.rewardPayPoint = rewardPayPoint;
        this.score = score;
    }
}
