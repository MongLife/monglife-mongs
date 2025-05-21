package com.monglife.mongs.adapter.out.mong.publish.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MongPublishDto {

    private Long mongId;

    private String mongName;

    private String mongTypeCode;

    private Integer payPoint;

    private String stateCode;

    private Boolean isSleep;

    private String statusCode;

    private Double weight;

    private Double expRatio;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public MongPublishDto(Long mongId, String mongName, String mongTypeCode, Integer payPoint, String stateCode, Boolean isSleep, String statusCode, Double weight, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
        this.statusCode = statusCode;
        this.weight = weight;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
