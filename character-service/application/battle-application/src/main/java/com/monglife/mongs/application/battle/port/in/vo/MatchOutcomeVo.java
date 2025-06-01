package com.monglife.mongs.application.battle.port.in.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchOutcomeVo {

    private final Double exp;

    private final Integer rewardPayPoint;

    private final Integer bettingPayPoint;

    @Builder
    public MatchOutcomeVo(Double exp, Integer rewardPayPoint, Integer bettingPayPoint) {
        this.exp = exp;
        this.rewardPayPoint = rewardPayPoint;
        this.bettingPayPoint = bettingPayPoint;
    }
}
