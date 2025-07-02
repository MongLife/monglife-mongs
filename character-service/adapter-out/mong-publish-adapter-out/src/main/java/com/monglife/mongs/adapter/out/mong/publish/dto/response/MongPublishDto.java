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

    private String name;

    private String mongCode;

    private String mongName;

    private Integer payPoint;

    private String stateCode;

    private String statusCode;

    private Boolean isSleep;

    private Double weight;

    private Double expRatio;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    private Integer poopCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public MongPublishDto(Long mongId, String name, String mongCode, String mongName, Integer payPoint, String stateCode, String statusCode, Boolean isSleep, Double weight, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.name = name;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.payPoint = payPoint;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.isSleep = isSleep;
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
}
