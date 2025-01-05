package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.domain.mong.dto.etc.GetMongDto;
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

    public static GetMongResponseDto of(GetMongDto getMongDto) {
        return GetMongResponseDto.builder()
                .mong(MongResponseDto.builder()
                        .mongId(getMongDto.getMongId())
                        .mongName(getMongDto.getMongName())
                        .mongTypeCode(getMongDto.getMongTypeCode())
                        .payPoint(getMongDto.getPayPoint())
                        .createdAt(getMongDto.getCreatedAt())
                        .updatedAt(getMongDto.getUpdatedAt())
                        .build())
                .mongState(MongStateResponseDto.builder()
                        .mongId(getMongDto.getMongId())
                        .stateCode(getMongDto.getStateCode())
                        .isSleep(getMongDto.getIsSleep())
                        .updatedAt(getMongDto.getUpdatedAt())
                        .build())
                .mongStatus(MongStatusResponseDto.builder()
                        .mongId(getMongDto.getMongId())
                        .statusCode(getMongDto.getStatusCode())
                        .expRatio(getMongDto.getExpRatio())
                        .weight(getMongDto.getWeight())
                        .strengthRatio(getMongDto.getStrengthRatio())
                        .satietyRatio(getMongDto.getSatietyRatio())
                        .healthyRatio(getMongDto.getHealthyRatio())
                        .fatigueRatio(getMongDto.getFatigueRatio())
                        .poopCount(getMongDto.getPoopCount())
                        .updatedAt(getMongDto.getUpdatedAt())
                        .build())
                .build();
    }

    public static List<GetMongResponseDto> toList(List<GetMongDto> getMongDtos) {
        return getMongDtos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
