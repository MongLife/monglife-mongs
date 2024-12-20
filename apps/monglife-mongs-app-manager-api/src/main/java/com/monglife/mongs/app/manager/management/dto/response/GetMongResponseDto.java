package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import lombok.*;

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

    @Builder
    public GetMongResponseDto(Long mongId, String mongName, String mongTypeCode, Integer payPoint, Double weight, Double expRatio, Double strengthRatio, Double satietyRatio, Double healthyRatio, Double fatigueRatio, Integer poopCount, MongStateCode stateCode, MongStatusCode statusCode, Boolean isSleep) {
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
                .build();
    }

    public static List<GetMongResponseDto> toList(List<GetMongDto> getMongDtos) {
        return getMongDtos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
