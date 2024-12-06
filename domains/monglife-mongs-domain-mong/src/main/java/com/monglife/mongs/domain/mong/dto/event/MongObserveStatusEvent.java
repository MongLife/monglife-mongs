package com.monglife.mongs.domain.mong.dto.event;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MongObserveStatusEvent {

    private final Long mongId;

    private final MongStatusCode statusCode;

    private final Double weight;

    private final Double expRatio;

    private final Double strengthRatio;

    private final Double satietyRatio;

    private final Double healthyRatio;

    private final Double fatigueRatio;

    private final Integer poopCount;

    private final MongStateCode stateCode;

    @Builder
    public MongObserveStatusEvent(Long mongId, MongStatusCode statusCode, Double weight, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, MongStateCode stateCode) {
        this.mongId = mongId;
        this.statusCode = statusCode;
        this.weight = weight;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.stateCode = stateCode;
    }
}
