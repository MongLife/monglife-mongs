package com.monglife.mongs.domain.mong.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongType {

    private final Long mongTypeId;

    private final String mongTypeCode;

    private final String mongTypeName;

    private final Integer level;

    private final Double evolutionScore;

    private final Double maxStatus;

    @Builder
    public MongType(Long mongTypeId, String mongTypeCode, String mongTypeName, Integer level, Double evolutionScore, Double maxStatus) {
        this.mongTypeId = mongTypeId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.level = level;
        this.evolutionScore = evolutionScore;
        this.maxStatus = maxStatus;
    }
}
