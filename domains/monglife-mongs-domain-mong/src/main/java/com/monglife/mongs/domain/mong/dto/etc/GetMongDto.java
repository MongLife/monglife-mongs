package com.monglife.mongs.domain.mong.dto.etc;

import com.monglife.mongs.domain.mong.entity.data.MongEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class GetMongDto {

    private Long mongId;

    private String mongName;

    private String mongTypeCode;

    private Integer payPoint;

    private Integer level;

    private Double weight;

    private Double strength;

    private Double satiety;

    private Double healthy;

    private Double fatigue;

    private Double expRatio;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    private Integer poopCount;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Boolean isSleep;

    private LocalTime sleepAt;

    private LocalTime wakeupAt;

    private Boolean isEgg;

    @Builder
    public GetMongDto(Long mongId, String mongName, String mongTypeCode, Integer payPoint, Integer level, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, MongStateCode stateCode, MongStatusCode statusCode, Boolean isSleep, LocalTime sleepAt, LocalTime wakeupAt, Boolean isEgg) {
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
    }

    public static GetMongDto of(MongEntity mongEntity) {
        return GetMongDto.builder()
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
                .build();
    }
}
