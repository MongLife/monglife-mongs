package com.monglife.mongs.app.activity.training.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingRunnerRequestDto {

    private Long mongId;

    private Integer score;

    @Builder
    public TrainingRunnerRequestDto(Long mongId, Integer score) {
        this.mongId = mongId;
        this.score = score;
    }
}
