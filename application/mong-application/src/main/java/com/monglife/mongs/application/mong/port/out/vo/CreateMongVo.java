package com.monglife.mongs.application.mong.port.out.vo;

import com.monglife.mongs.domain.enums.MongStateCode;
import com.monglife.mongs.domain.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateMongVo {

    private final Long accountId;

    private final String mongName;

    private final String mongTypeCode;

    private final MongStatusCode statusCode;

    private final MongStateCode stateCode;

    private final Integer level;

    private final Double maxStatus;

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

    private final Integer poopCount;

    @Builder
    public CreateMongVo(Long accountId, String mongName, String mongTypeCode, MongStatusCode statusCode, MongStateCode stateCode, Integer level, Double maxStatus, LocalTime sleepAt, LocalTime wakeupAt, Integer payPoint, Boolean isSleep, Double strength, Double satiety, Double healthy, Double fatigue, Double exp, Double weight, Integer poopCount) {
        this.accountId = accountId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.statusCode = statusCode;
        this.stateCode = stateCode;
        this.level = level;
        this.maxStatus = maxStatus;
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
        this.poopCount = poopCount;
    }
}
