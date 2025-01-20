package com.monglife.mongs.domain.mong.vo;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class MongVo {

    /**
     * Mong Basic
     */
    private final Long mongId;

    private final String mongName;

    private final String mongTypeCode;

    private final Integer level;

    private final LocalTime sleepAt;

    private final LocalTime wakeupAt;

    private final Boolean isEgg;

    private final Integer payPoint;

    private final LocalDateTime createdAt;

    private final LocalDateTime basicUpdatedAt;

    /**
     * Mong State
     */
    private final MongStateCode stateCode;

    private final Boolean isSleep;

    private final LocalDateTime stateUpdatedAt;

    /**
     * Mong Status
     */
    private final MongStatusCode statusCode;

    private final Double strength;

    private final Double satiety;

    private final Double healthy;

    private final Double fatigue;

    private final Double expRatio;

    private final Double weight;

    private final Double strengthRatio;

    private final Double satietyRatio;

    private final Double healthyRatio;

    private final Double fatigueRatio;

    private final Integer poopCount;

    private final LocalDateTime statusUpdatedAt;

    @Builder
    public MongVo(Long mongId, String mongName, String mongTypeCode, Integer level, LocalTime sleepAt, LocalTime wakeupAt, Boolean isEgg, Integer payPoint, LocalDateTime createdAt, LocalDateTime basicUpdatedAt, MongStateCode stateCode, Boolean isSleep, LocalDateTime stateUpdatedAt, MongStatusCode statusCode, Double strength, Double satiety, Double healthy, Double fatigue, Double expRatio, Double weight, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, LocalDateTime statusUpdatedAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.level = level;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.isEgg = isEgg;
        this.payPoint = payPoint;
        this.createdAt = createdAt;
        this.basicUpdatedAt = basicUpdatedAt;

        this.stateCode = stateCode;
        this.isSleep = isSleep;
        this.stateUpdatedAt = stateUpdatedAt;

        this.statusCode = statusCode;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.expRatio = expRatio;
        this.weight = weight;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public static MongVo of(MongEntity mongEntity) {

        return MongVo.builder()
                // Mong Basic
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongTypeCode(mongEntity.getType().getComn().getCode())
                .level(mongEntity.getType().getLevel())
                .sleepAt(mongEntity.getSleepAt())
                .wakeupAt(mongEntity.getWakeupAt())
                .isEgg(mongEntity.isEgg())
                .payPoint(mongEntity.getPayPoint())
                .createdAt(mongEntity.getCreatedAt())
                .basicUpdatedAt(mongEntity.getUpdatedAt())
                // Mong State
                .stateCode(mongEntity.getState().getCode())
                .isSleep(mongEntity.getState().getIsSleep())
                .stateUpdatedAt(mongEntity.getState().getUpdatedAt())
                // Mong Status
                .statusCode(mongEntity.getStatus().getCode())
                .strength(mongEntity.getStatus().getStrength())
                .satiety(mongEntity.getStatus().getSatiety())
                .healthy(mongEntity.getStatus().getHealthy())
                .fatigue(mongEntity.getStatus().getFatigue())
                .expRatio(mongEntity.getStatus().getExpRatio())
                .weight(mongEntity.getStatus().getWeight())
                .strengthRatio(mongEntity.getStatus().getStrengthRatio())
                .satietyRatio(mongEntity.getStatus().getSatietyRatio())
                .healthyRatio(mongEntity.getStatus().getHealthyRatio())
                .fatigueRatio(mongEntity.getStatus().getFatigueRatio())
                .poopCount(mongEntity.getStatus().getPoopCount())
                .statusUpdatedAt(mongEntity.getState().getUpdatedAt())
                .build();
    }
}
