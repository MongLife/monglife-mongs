package com.monglife.mongs.adapter.in.mong.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateMongResponseDto {

    private Long mongId;

    private String name;

    private String mongCode;

    private String mongName;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Integer level;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime sleepAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime wakeupAt;

    private Integer payPoint;

    private Boolean isSleep;

    private Double strengthRatio;

    private Double healthyRatio;

    private Double satietyRatio;

    private Double fatigueRatio;

    private Double expRatio;

    private Double weight;

    private Integer poopCount;

    private Integer randomDrawTicketCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public CreateMongResponseDto(Long mongId, String name, String mongCode, String mongName, MongStateCode stateCode, MongStatusCode statusCode, Integer level, LocalTime sleepAt, LocalTime wakeupAt, Integer payPoint, Boolean isSleep, Double strengthRatio, Double healthyRatio, Double satietyRatio, Double fatigueRatio, Double expRatio, Double weight, Integer poopCount, Integer randomDrawTicketCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.name = name;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.level = level;
        this.sleepAt = sleepAt;
        this.wakeupAt = wakeupAt;
        this.payPoint = payPoint;
        this.isSleep = isSleep;
        this.strengthRatio = strengthRatio;
        this.healthyRatio = healthyRatio;
        this.satietyRatio = satietyRatio;
        this.fatigueRatio = fatigueRatio;
        this.expRatio = expRatio;
        this.weight = weight;
        this.poopCount = poopCount;
        this.randomDrawTicketCount = randomDrawTicketCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
