package com.monglife.mongs.app.activity.training.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingRunnerRequestDto {

    private Integer score;

    @Builder
    public TrainingRunnerRequestDto(Integer score) {
        this.score = score;
    }
}
