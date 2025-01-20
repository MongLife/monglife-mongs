package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MongStatusObserveEvent {

    private final Long mongId;

    private final MongStateCode stateCode;

    private final MongStatusCode statusCode;

    private final Double expRatio;

    private final Double weight;

    private final Double strengthRatio;

    private final Double satietyRatio;

    private final Double healthyRatio;

    private final Double fatigueRatio;

    private final Integer poopCount;

    private final LocalDateTime updatedAt;

    @Builder
    public MongStatusObserveEvent(Long mongId, MongStateCode stateCode, MongStatusCode statusCode, Double expRatio, Double weight, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.expRatio = expRatio;
        this.weight = weight;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.updatedAt = updatedAt;
    }
}
