package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MatchMong {

    private final Long mongId;

    private final Long accountId;

    private final String mongName;

    private final String mongTypeCode;

    private final String mongTypeName;

    private final Integer level;

    private Integer payPoint;

    private final Boolean isSleep;

    private final Double strength;

    private final Double satiety;

    private final Double healthy;

    private final Double fatigue;

    private Double exp;

    private final Double weight;

    @Builder
    public MatchMong(Long mongId, Long accountId, String mongName, String mongTypeCode, String mongTypeName, Boolean isSleep, Double strength, Double satiety, Double healthy, Double fatigue, Double weight, Double exp, Integer payPoint, Integer level) {
        this.mongId = mongId;
        this.accountId = accountId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.isSleep = isSleep;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.weight = weight;
        this.exp = exp;
        this.payPoint = payPoint;
        this.level = level;
    }
}
