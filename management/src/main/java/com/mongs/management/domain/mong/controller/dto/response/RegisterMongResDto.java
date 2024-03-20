package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.entity.Mong;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMongResDto(
        Long mongId,
        String mongCode,
        Double weight,
        Double health,
        Double satiety,
        Double strength,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping,
        Double exp,
        LocalDateTime born,
        String stateCode,
        String shiftCode,
        Integer payPoint
) {
    public static RegisterMongResDto of(Mong mong) {
        return RegisterMongResDto.builder()
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
}
