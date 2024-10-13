package com.monglife.mongs.app.management.presentation.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindMongResDto(
        Long mongId,
        String name,
        String mongCode,
        Double weight,
        Double healthy,
        Double satiety,
        Double strength,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping,
        Double exp,
        String stateCode,
        String shiftCode,
        Integer payPoint,
        LocalDateTime born
) {
}
