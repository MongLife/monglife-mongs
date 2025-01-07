package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MongObserveEvent {

    private final Long mongId;

    private final String mongName;

    private final Integer payPoint;

    private final MongStateCode stateCode;

    private final String mongTypeCode;

    private final Boolean isSleep;

    private final MongStatusCode statusCode;

    private final Double weight;

    private final Double expRatio;

    private final Double strengthRatio;

    private final Double satietyRatio;

    private final Double healthyRatio;

    private final Double fatigueRatio;

    private final Integer poopCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public MongObserveEvent(Long mongId, String mongName, Integer payPoint, MongStateCode stateCode, String mongTypeCode, Boolean isSleep, MongStatusCode statusCode, Double weight, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.payPoint = payPoint;
        this.stateCode = stateCode;
        this.mongTypeCode = mongTypeCode;
        this.isSleep = isSleep;
        this.statusCode = statusCode;
        this.weight = weight;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MongObserveEvent of(MongEntity mongEntity) {
        return MongObserveEvent.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .payPoint(mongEntity.getPayPoint())
                .mongTypeCode(mongEntity.getType().getComn().getCode())
                .stateCode(mongEntity.getState().getCode())
                .isSleep(mongEntity.getState().getIsSleep())
                .statusCode(mongEntity.getStatus().getCode())
                .weight(mongEntity.getStatus().getWeight())
                .expRatio(mongEntity.getStatus().getExpRatio())
                .strengthRatio(mongEntity.getStatus().getStrengthRatio())
                .satietyRatio(mongEntity.getStatus().getSatietyRatio())
                .healthyRatio(mongEntity.getStatus().getHealthyRatio())
                .fatigueRatio(mongEntity.getStatus().getFatigueRatio())
                .poopCount(mongEntity.getStatus().getPoopCount())
                .createdAt(mongEntity.getCreatedAt())
                .updatedAt(mongEntity.getUpdatedAt())
                .build();
    }
}
