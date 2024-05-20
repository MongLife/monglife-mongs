package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMongResDto(
        Long accountId,
        Long mongId,
        String name,
        String mongCode,
        Double weight,
        Double healthy,
        Double satiety,
        Double strength,
        Double sleep,
        Double exp,
        Integer poopCount,
        Boolean isSleeping,
        String stateCode,
        String shiftCode,
        Integer payPoint,
        LocalDateTime born
) {
}
