package com.monglife.mongs.app.activity.training.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetTrainingResponseDto {

    private Integer rewardPayPoint;

    private Integer score;

    private Integer timeout;

    @Builder
    public GetTrainingResponseDto(Integer rewardPayPoint, Integer score, Integer timeout) {
        this.rewardPayPoint = rewardPayPoint;
        this.score = score;
        this.timeout = timeout;
    }
}
