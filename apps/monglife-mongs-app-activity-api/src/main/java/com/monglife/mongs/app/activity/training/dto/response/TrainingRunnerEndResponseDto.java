package com.monglife.mongs.app.activity.training.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingRunnerEndResponseDto {

    private Boolean isSuccess;

    private Integer score;

    private Integer rewardPayPoint;

    @Builder
    public TrainingRunnerEndResponseDto(Boolean isSuccess, Integer score, Integer rewardPayPoint) {
        this.isSuccess = isSuccess;
        this.score = score;
        this.rewardPayPoint = rewardPayPoint;
    }
}
