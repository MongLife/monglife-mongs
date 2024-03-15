package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

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
}
