package com.monglife.mongs.adapter.in.mong.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingEndRequestDto {

    private String trainingTypeCode;

    private Long mongId;

    private Integer score;

    @Builder
    public TrainingEndRequestDto(String trainingTypeCode, Long mongId, Integer score) {
        this.trainingTypeCode = trainingTypeCode;
        this.mongId = mongId;
        this.score = score;
    }
}
