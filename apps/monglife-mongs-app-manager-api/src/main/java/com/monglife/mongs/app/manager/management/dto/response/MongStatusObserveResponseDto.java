package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongStatusObserveResponseDto {

    private MongStatusCode statusCode;

    private Double exp;

    private Double weight;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    private Integer poopCount;

    @Builder
    public MongStatusObserveResponseDto(MongStatusCode statusCode, Double exp, Double weight, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount) {
        this.statusCode = statusCode;
        this.exp = exp;
        this.weight = weight;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
    }
}
