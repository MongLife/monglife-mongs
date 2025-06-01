package com.monglife.mongs.adapter.in.mong.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetMongResponseDto {

    private Long mongId;

    private String name;

    private String mongCode;

    private Integer payPoint;

    private Double expRatio;

    private Double strengthRatio;

    private Double healthyRatio;

    private Double satietyRatio;

    private Double fatigueRatio;

    private Double weight;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Integer poopCount;

    private Boolean isSleep;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public GetMongResponseDto(Long mongId, String name, String mongCode, Integer payPoint, Double expRatio, Double strengthRatio, Double healthyRatio, Double satietyRatio, Double fatigueRatio, Double weight, MongStateCode stateCode, MongStatusCode statusCode, Integer poopCount, Boolean isSleep, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.name = name;
        this.mongCode = mongCode;
        this.payPoint = payPoint;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.healthyRatio = healthyRatio;
        this.satietyRatio = satietyRatio;
        this.fatigueRatio = fatigueRatio;
        this.weight = weight;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.poopCount = poopCount;
        this.isSleep = isSleep;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
