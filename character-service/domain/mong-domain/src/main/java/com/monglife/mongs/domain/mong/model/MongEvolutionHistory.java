package com.monglife.mongs.domain.mong.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MongEvolutionHistory {

    private final Long mongEvolutionHistoryId;

    private final String mongCode;

    private final Long accountId;

    private final Double evolutionScore;

    @Builder
    public MongEvolutionHistory(Long mongEvolutionHistoryId, String mongCode, Long accountId, Double evolutionScore) {
        this.mongEvolutionHistoryId = mongEvolutionHistoryId;
        this.mongCode = mongCode;
        this.accountId = accountId;
        this.evolutionScore = evolutionScore;
    }
}
