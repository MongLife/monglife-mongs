package com.monglife.mongs.app.manager.management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetMongResponseDto {

    private Long mongId;

    private String mongName;

    private String mongTypeCode;

    private Integer payPoint;

    private Double weight;

    private Double expRatio;

    private Double strengthRatio;

    private Double satietyRatio;

    private Double healthyRatio;

    private Double fatigueRatio;

    private Integer poopCount;

    private MongStateCode stateCode;

    private MongStatusCode statusCode;

    private Boolean isSleep;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public GetMongResponseDto(Long mongId, String mongName, String mongTypeCode, Integer payPoint, Double weight, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, MongStateCode stateCode, MongStatusCode statusCode, Boolean isSleep, LocalDateTime createdAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
        this.weight = weight;
        this.expRatio = expRatio;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
        this.isSleep = isSleep;
        this.createdAt = createdAt;
    }

    public static GetMongResponseDto of(GetMongDto getMongDto) {
        return GetMongResponseDto.builder()
                .mongId(getMongDto.getMongId())
                .mongName(getMongDto.getMongName())
                .mongTypeCode(getMongDto.getMongTypeCode())
                .payPoint(getMongDto.getPayPoint())
                .weight(getMongDto.getWeight())
                .expRatio(getMongDto.getExpRatio())
                .strengthRatio(getMongDto.getStrengthRatio())
                .satietyRatio(getMongDto.getSatietyRatio())
                .healthyRatio(getMongDto.getHealthyRatio())
                .fatigueRatio(getMongDto.getFatigueRatio())
                .poopCount(getMongDto.getPoopCount())
                .stateCode(getMongDto.getStateCode())
                .statusCode(getMongDto.getStatusCode())
                .isSleep(getMongDto.getIsSleep())
                .createdAt(getMongDto.getCreatedAt())
                .build();
    }

    public static List<GetMongResponseDto> toList(List<GetMongDto> getMongDtos) {
        return getMongDtos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
