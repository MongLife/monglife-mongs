package com.monglife.mongs.app.manager.management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MongStatusResponseDto {

    private Long mongId;

    private MongStatusCode statusCode;

    private Double expRatio;

    private Double weight;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    private Integer poopCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public MongStatusResponseDto(Long mongId, MongStatusCode statusCode, Double expRatio, Double weight, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, LocalDateTime updatedAt) {
        this.mongId = mongId;
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
