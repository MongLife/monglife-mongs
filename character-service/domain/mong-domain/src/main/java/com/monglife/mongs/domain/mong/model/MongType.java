package com.monglife.mongs.domain.mong.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MongType {

    private final Long mongTypeId;

    private final String mongCode;

    private final String mongName;

    private final Integer level;

    private final Double evolutionScore;

    private final Double maxStatus;

    @Builder
    public MongType(Long mongTypeId, String mongCode, String mongName, Integer level, Double evolutionScore, Double maxStatus) {
        this.mongTypeId = mongTypeId;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.level = level;
        this.evolutionScore = evolutionScore;
        this.maxStatus = maxStatus;
    }
}
