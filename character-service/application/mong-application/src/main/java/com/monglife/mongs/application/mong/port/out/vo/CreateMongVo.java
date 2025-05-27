package com.monglife.mongs.application.mong.port.out.vo;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.model.MongType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateMongVo {

    private final Long accountId;

    private final String name;

    private final MongStatusCode statusCode;

    private final MongStateCode stateCode;

    private final LocalTime sleepAt;

    private final LocalTime wakeupAt;

    private final Integer payPoint;

    private final Boolean isSleep;

    private final Double strength;

    private final Double satiety;

    private final Double healthy;

    private final Double fatigue;

    private final Double exp;

    private final Double weight;

    private final Double evolutionReward;

    private final Double evolutionPenalty;

    private final Integer strokeCount;

    private final Integer trainingCount;

    private final Integer poopCount;

    private final Integer randomDrawTicketCount;

    private final MongType mongType;

    @Builder
    public CreateMongVo(Long accountId, String name, MongStatusCode statusCode, MongStateCode stateCode, LocalTime sleepAt, LocalTime wakeupAt, Integer payPoint, Boolean isSleep, Double strength, Double satiety, Double healthy, Double fatigue, Double exp, Double weight, Double evolutionReward, Double evolutionPenalty, Integer strokeCount, Integer trainingCount, Integer poopCount, Integer randomDrawTicketCount, MongType mongType) {
        this.accountId = accountId;
        this.name = name;
        this.statusCode = statusCode;
        this.stateCode = stateCode;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.payPoint = payPoint;
        this.isSleep = isSleep;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.exp = exp;
        this.weight = weight;
        this.evolutionReward = evolutionReward;
        this.evolutionPenalty = evolutionPenalty;
        this.strokeCount = strokeCount;
        this.trainingCount = trainingCount;
        this.poopCount = poopCount;
        this.randomDrawTicketCount = randomDrawTicketCount;
        this.mongType = mongType;
    }
}
