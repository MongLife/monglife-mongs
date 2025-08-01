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

    private Double evolutionScore;

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

    /**
     * 진화 스코어 패치
     * @param decreasePercent 감소 비율 (0 초과 1 이하의 값)
     */
    public void fetchEvolutionScore(Double decreasePercent) {
        if (0 < decreasePercent && decreasePercent <= 1) {
            this.evolutionScore = Math.max(0, this.evolutionScore * (1 - decreasePercent));
        }
    }
}
