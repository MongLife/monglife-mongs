package com.monglife.mongs.adapter.in.mong.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetTrainingTypeResponseDto {

    private Long trainingTypeId;

    private String trainingCode;

    private String trainingName;

    private Integer payPoint;

    private Integer score;

    private Integer timeout;

    private Double exp;

    private Double strength;

    private Double weight;

    private Double satiety;

    private Double fatigue;

    @Builder
    public GetTrainingTypeResponseDto(Long trainingTypeId, String trainingCode, String trainingName, Integer payPoint, Integer score, Integer timeout, Double exp, Double strength, Double weight, Double satiety, Double fatigue) {
        this.trainingTypeId = trainingTypeId;
        this.trainingCode = trainingCode;
        this.trainingName = trainingName;
        this.payPoint = payPoint;
        this.score = score;
        this.timeout = timeout;
        this.exp = exp;
        this.strength = strength;
        this.weight = weight;
        this.satiety = satiety;
        this.fatigue = fatigue;
    }
}
