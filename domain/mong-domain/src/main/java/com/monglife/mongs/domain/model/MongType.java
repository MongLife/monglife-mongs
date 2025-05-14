package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MongType {

    private Long mongTypeId;

    private String mongTypeCode;

    private String mongTypeName;

    private Integer level;

    private Double evolutionScore;

    private Double maxStatus;

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
