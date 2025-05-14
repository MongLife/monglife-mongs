package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TrainingType {

    private Long trainingTypeId;

    private String trainingTypeCode;

    private String trainingTypeName;

    private Integer payPoint;

    private Integer score;

    private Integer timeout;

    private Double exp;

    private Double strength;

    private Double weight;

    private Double satiety;

    private Double fatigue;

    @Builder
    public TrainingType(Long trainingTypeId, String trainingTypeCode, String trainingTypeName, Integer payPoint, Integer score, Integer timeout, Double exp, Double strength, Double weight, Double satiety, Double fatigue) {
        this.trainingTypeId = trainingTypeId;
        this.trainingTypeCode = trainingTypeCode;
        this.trainingTypeName = trainingTypeName;
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
