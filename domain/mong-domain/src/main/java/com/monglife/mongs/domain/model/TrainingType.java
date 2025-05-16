package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TrainingType {

    private final Long trainingTypeId;

    private final String trainingTypeCode;

    private final String trainingTypeName;

    private final Integer payPoint;

    private final Integer score;

    private final Integer timeout;

    private final Double exp;

    private final Double strength;

    private final Double weight;

    private final Double satiety;

    private final Double fatigue;

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
