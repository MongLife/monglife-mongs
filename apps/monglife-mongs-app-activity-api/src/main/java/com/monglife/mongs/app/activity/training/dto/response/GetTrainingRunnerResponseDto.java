package com.monglife.mongs.app.activity.training.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetTrainingRunnerResponseDto {

    private Integer payPoint;

    @Builder
    public GetTrainingRunnerResponseDto(Integer payPoint) {
        this.payPoint = payPoint;
    }
}
