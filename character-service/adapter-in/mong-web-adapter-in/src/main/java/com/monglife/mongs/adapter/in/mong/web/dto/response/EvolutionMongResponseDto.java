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
public class EvolutionMongResponseDto {

    private Long mongId;

    private String mongCode;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Double expRatio;

    private Double strengthRatio;

    private Double healthyRatio;

    private Double satietyRatio;

    private Double fatigueRatio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public EvolutionMongResponseDto(Long mongId, String mongCode, MongStateCode stateCode, MongStatusCode statusCode, Double expRatio, Double strengthRatio, Double healthyRatio, Double satietyRatio, Double fatigueRatio, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.mongCode = mongCode;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.healthyRatio = healthyRatio;
        this.satietyRatio = satietyRatio;
        this.fatigueRatio = fatigueRatio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
