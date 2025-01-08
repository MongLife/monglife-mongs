package com.monglife.mongs.app.manager.management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.vo.MongVo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GetMongResponseDto {

    private Long mongId;

    private String mongName;

    private String mongTypeCode;

    private Integer payPoint;

    private MongStateCode stateCode;

    private Boolean isSleep;

    private MongStatusCode statusCode;

    private Double expRatio;

    private Double weight;

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
    public GetMongResponseDto(Long mongId, String mongName, String mongTypeCode, Integer payPoint, MongStateCode stateCode, Boolean isSleep, MongStatusCode statusCode, Double expRatio, Double weight, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.mongName = mongName;
        this.mongTypeCode = mongTypeCode;
        this.payPoint = payPoint;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
        this.statusCode = statusCode;
        this.expRatio = expRatio;
        this.weight = weight;
        this.strengthRatio = strengthRatio;
        this.satietyRatio = satietyRatio;
        this.healthyRatio = healthyRatio;
        this.fatigueRatio = fatigueRatio;
        this.poopCount = poopCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetMongResponseDto of(MongObserveEvent event) {
        return GetMongResponseDto.builder()
                .mongId(event.getMongId())
                .mongName(event.getMongName())
                .mongTypeCode(event.getMongTypeCode())
                .payPoint(event.getPayPoint())
                .stateCode(event.getStateCode())
                .isSleep(event.getIsSleep())
                .statusCode(event.getStatusCode())
                .expRatio(event.getExpRatio())
                .weight(event.getWeight())
                .strengthRatio(event.getStrengthRatio())
                .satietyRatio(event.getSatietyRatio())
                .healthyRatio(event.getHealthyRatio())
                .fatigueRatio(event.getFatigueRatio())
                .poopCount(event.getPoopCount())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }

    public static GetMongResponseDto of(MongVo mongVo) {
        return GetMongResponseDto.builder()
                .mongId(mongVo.getMongId())
                .mongName(mongVo.getMongName())
                .mongTypeCode(mongVo.getMongTypeCode())
                .payPoint(mongVo.getPayPoint())
                .stateCode(mongVo.getStateCode())
                .isSleep(mongVo.getIsSleep())
                .statusCode(mongVo.getStatusCode())
                .expRatio(mongVo.getExpRatio())
                .weight(mongVo.getWeight())
                .strengthRatio(mongVo.getStrengthRatio())
                .satietyRatio(mongVo.getSatietyRatio())
                .healthyRatio(mongVo.getHealthyRatio())
                .fatigueRatio(mongVo.getFatigueRatio())
                .poopCount(mongVo.getPoopCount())
                .createdAt(mongVo.getCreatedAt())
                .updatedAt(mongVo.getUpdatedAt())
                .build();
    }

    public static List<GetMongResponseDto> toList(List<MongVo> mongVos) {
        return mongVos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
