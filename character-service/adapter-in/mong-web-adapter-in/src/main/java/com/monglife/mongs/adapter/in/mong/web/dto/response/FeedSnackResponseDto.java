package com.monglife.mongs.adapter.in.mong.web.dto.response;

import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedSnackResponseDto {

    private Long mongId;

    private Integer payPoint;

    private Double expRatio;

    private Double strengthRatio;

    private Double healthyRatio;

    private Double satietyRatio;

    private Double fatigueRatio;

    private Double weight;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    @Builder
    public FeedSnackResponseDto(Long mongId, Integer payPoint, Double expRatio, Double strengthRatio, Double healthyRatio, Double satietyRatio, Double fatigueRatio, Double weight, MongStateCode stateCode, MongStatusCode statusCode) {
        this.mongId = mongId;
        this.payPoint = payPoint;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.healthyRatio = healthyRatio;
        this.satietyRatio = satietyRatio;
        this.fatigueRatio = fatigueRatio;
        this.weight = weight;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
    }
}
