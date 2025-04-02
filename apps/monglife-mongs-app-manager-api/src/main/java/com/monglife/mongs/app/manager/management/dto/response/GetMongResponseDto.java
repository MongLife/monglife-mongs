package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.app.manager.management.dto.etc.MongBasicDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStateDto;
import com.monglife.mongs.app.manager.management.dto.etc.MongStatusDto;
import com.monglife.mongs.domain.mong.vo.MongVo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GetMongResponseDto {

    private MongBasicDto basic;

    private MongStateDto state;

    private MongStatusDto status;

    @Builder
    public GetMongResponseDto(MongBasicDto basic, MongStateDto state, MongStatusDto status) {
        this.basic = basic;
        this.state = state;
        this.status = status;
    }

    public static GetMongResponseDto of(MongVo mongVo) {
        return GetMongResponseDto.builder()
                .basic(MongBasicDto.builder()
                        .mongId(mongVo.getMongId())
                        .mongName(mongVo.getMongName())
                        .mongTypeCode(mongVo.getMongTypeCode())
                        .payPoint(mongVo.getPayPoint())
                        .createdAt(mongVo.getCreatedAt())
                        .updatedAt(mongVo.getBasicUpdatedAt())
                        .build())
                .state(MongStateDto.builder()
                        .mongId(mongVo.getMongId())
                        .stateCode(mongVo.getStateCode())
                        .isSleep(mongVo.getIsSleep())
                        .updatedAt(mongVo.getStateUpdatedAt())
                        .build())
                .status(MongStatusDto.builder()
                        .mongId(mongVo.getMongId())
                        .statusCode(mongVo.getStatusCode())
                        .expRatio(mongVo.getExpRatio())
                        .weight(mongVo.getWeight())
                        .strengthRatio(mongVo.getStrengthRatio())
                        .satietyRatio(mongVo.getSatietyRatio())
                        .healthyRatio(mongVo.getHealthyRatio())
                        .fatigueRatio(mongVo.getFatigueRatio())
                        .poopCount(mongVo.getPoopCount())
                        .updatedAt(mongVo.getStatusUpdatedAt())
                        .build())
                .build();
    }

    public static List<GetMongResponseDto> toList(List<MongVo> mongVos) {
        return mongVos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
