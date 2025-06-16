package com.monglife.mongs.application.mong.port.in.vo;

import com.monglife.mongs.domain.mong.model.Mong;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainingEndVo {

    private final Boolean isSuccess;

    private final Integer rewardPayPoint;

    private final Integer score;

    private final Mong mong;

    @Builder
    public TrainingEndVo(Boolean isSuccess, Integer rewardPayPoint, Integer score, Mong mong) {
        this.isSuccess = isSuccess;
        this.rewardPayPoint = rewardPayPoint;
        this.score = score;
        this.mong = mong;
    }
}
