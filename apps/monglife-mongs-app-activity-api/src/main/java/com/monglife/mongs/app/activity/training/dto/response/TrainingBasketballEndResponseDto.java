package com.monglife.mongs.app.activity.training.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingBasketballEndResponseDto {

    private Boolean isSuccess;

    private Integer score;

    private Integer rewardPayPoint;

    @Builder
    public TrainingBasketballEndResponseDto(Boolean isSuccess, Integer score, Integer rewardPayPoint) {
        this.isSuccess = isSuccess;
        this.score = score;
        this.rewardPayPoint = rewardPayPoint;
    }
}
