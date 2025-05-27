package com.monglife.mongs.domain.battle.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GenerateMatchPlayerVo {

    private final Long accountId;

    private final Long mongId;

    private final String mongCode;

    private final String mongName;

    private final String name;

    private final Double strength;

    private final Double fatigue;

    private final Double weight;

    @Builder
    public GenerateMatchPlayerVo(Long accountId, Long mongId, String mongCode, String mongName, String name, Double strength, Double fatigue, Double weight) {
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.name = name;
        this.strength = strength;
        this.fatigue = fatigue;
        this.weight = weight;
    }
}
