package com.monglife.mongs.domain.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GenerateMatchPlayerVo {

    private final Long accountId;

    private final Long mongId;

    private final String mongTypeCode;

    private final String mongTypeName;

    private final String mongName;

    private final Double strength;

    private final Double fatigue;

    private final Double weight;

    @Builder
    public GenerateMatchPlayerVo(Long accountId, Long mongId, String mongTypeCode, String mongTypeName, String mongName, Double strength, Double fatigue, Double weight) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.mongName = mongName;
        this.strength = strength;
        this.fatigue = fatigue;
        this.weight = weight;
    }
}
