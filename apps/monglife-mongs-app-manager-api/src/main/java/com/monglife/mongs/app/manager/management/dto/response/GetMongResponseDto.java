package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.vo.MongVo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GetMongResponseDto {

    private MongResponseDto mong;

    private MongStateResponseDto mongState;

    private MongStatusResponseDto mongStatus;

    @Builder
    public GetMongResponseDto(MongResponseDto mong, MongStateResponseDto mongState, MongStatusResponseDto mongStatus) {
        this.mong = mong;
        this.mongState = mongState;
        this.mongStatus = mongStatus;
    }

    public static GetMongResponseDto of(MongVo mongVo) {
        return GetMongResponseDto.builder()
                .mong(MongResponseDto.builder()
                        .mongId(mongVo.getMongId())
                        .mongName(mongVo.getMongName())
                        .mongTypeCode(mongVo.getMongTypeCode())
                        .payPoint(mongVo.getPayPoint())
                        .createdAt(mongVo.getCreatedAt())
                        .updatedAt(mongVo.getUpdatedAt())
                        .build())
                .mongState(MongStateResponseDto.builder()
                        .mongId(mongVo.getMongId())
                        .stateCode(mongVo.getStateCode())
                        .isSleep(mongVo.getIsSleep())
                        .updatedAt(mongVo.getUpdatedAt())
                        .build())
                .mongStatus(MongStatusResponseDto.builder()
                        .mongId(mongVo.getMongId())
                        .statusCode(mongVo.getStatusCode())
                        .expRatio(mongVo.getExpRatio())
                        .weight(mongVo.getWeight())
                        .strengthRatio(mongVo.getStrengthRatio())
                        .satietyRatio(mongVo.getSatietyRatio())
                        .healthyRatio(mongVo.getHealthyRatio())
                        .fatigueRatio(mongVo.getFatigueRatio())
                        .poopCount(mongVo.getPoopCount())
                        .updatedAt(mongVo.getUpdatedAt())
                        .build())
                .build();
    }

    public static List<GetMongResponseDto> toList(List<MongVo> mongVos) {
        return mongVos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
