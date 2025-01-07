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

    private final Long mongId;

    private final String mongName;

    private final String mongTypeCode;

    private final Integer payPoint;

    private final Integer level;

    private final Double weight;

    private final Double strength;

    private final Double satiety;

    private final Double healthy;

    private final Double fatigue;

    private final Double expRatio;

    private final Double strengthRatio;

    private final Double satietyRatio;

    private final Double healthyRatio;

    private final Double fatigueRatio;

    private final Integer poopCount;

    private final MongStateCode stateCode;

    private final MongStatusCode statusCode;

    private final Boolean isSleep;

    private final LocalTime sleepAt;

    private final LocalTime wakeupAt;

    private final Boolean isEgg;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public MongVo(Long mongId, String mongName, String mongTypeCode, Integer payPoint, Integer level, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, MongStateCode stateCode, MongStatusCode statusCode, Boolean isSleep, LocalTime sleepAt, LocalTime wakeupAt, Boolean isEgg, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
        this.level = level;
        this.weight = weight;
        this.strength = strength;
        this.satiety = satiety;
        this.healthy = healthy;
        this.fatigue = fatigue;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.isSleep = isSleep;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.isEgg = isEgg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MongVo of(MongEntity mongEntity) {

        return MongVo.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongTypeCode(mongEntity.getType().getComn().getCode())
                .payPoint(mongEntity.getPayPoint())
                .level(mongEntity.getType().getLevel())
                .weight(mongEntity.getStatus().getWeight())
                .strength(mongEntity.getStatus().getStrength())
                .satiety(mongEntity.getStatus().getSatiety())
                .healthy(mongEntity.getStatus().getHealthy())
                .fatigue(mongEntity.getStatus().getFatigue())
                .expRatio(mongEntity.getStatus().getExpRatio())
                .strengthRatio(mongEntity.getStatus().getStrengthRatio())
                .satietyRatio(mongEntity.getStatus().getSatietyRatio())
                .healthyRatio(mongEntity.getStatus().getHealthyRatio())
                .fatigueRatio(mongEntity.getStatus().getFatigueRatio())
                .poopCount(mongEntity.getStatus().getPoopCount())
                .stateCode(mongEntity.getState().getCode())
                .statusCode(mongEntity.getStatus().getCode())
                .isSleep(mongEntity.getState().getIsSleep())
                .sleepAt(mongEntity.getSleepAt())
                .wakeupAt(mongEntity.getWakeupAt())
                .isEgg(mongEntity.isEgg())
                .createdAt(mongEntity.getCreatedAt())
                .updatedAt(mongEntity.getUpdatedAt())
                .build();
    }
}
