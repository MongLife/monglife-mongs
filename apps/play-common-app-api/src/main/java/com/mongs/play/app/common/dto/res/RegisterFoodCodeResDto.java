package com.mongs.play.app.common.dto.res;

import lombok.Builder;

@Builder
public record RegisterFoodCodeResDto(
        String name,
        String code,
        String groupCode,
        Integer price,
        Double addWeightValue,
        Double addStrengthValue,
        Double addSatietyValue,
        Double addHealthyValue,
        Double addSleepValue,
        Integer delaySeconds
) {
}
