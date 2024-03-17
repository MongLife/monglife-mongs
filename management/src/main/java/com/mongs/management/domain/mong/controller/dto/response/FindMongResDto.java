package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.entity.Mong;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FindMongResDto(
        Long mongId,
        String mongCode,
        Double weight,
        Double health,
        Double satiety,
        Double strength,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping,
        Integer exp,
        LocalDateTime born,
        String stateCode,
        String shiftCode,
        Integer payPoint
) {
    public static FindMongResDto of(Mong mong) {
        return FindMongResDto.builder()
                .mongId(mong.getId())
                .mongCode(mong.getMongCode())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .satiety(mong.getSatiety())
                .strength(mong.getStrength())
                .sleep(mong.getSleep())
                .poopCount(mong.getNumberOfPoop())
                .isSleeping(mong.getIsSleeping())
                .exp(mong.getExp())
                .born(mong.getCreatedAt())
                .stateCode(mong.getState().getCode())
                .shiftCode(mong.getShift().getCode())
                .payPoint(mong.getPayPoint())
                .build();
    }

    public static List<FindMongResDto> toList(List<Mong> mongList) {
        return mongList.stream()
                .map(FindMongResDto::of)
                .toList();
    }
}
