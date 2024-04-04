package com.mongs.common.service.vo;

import lombok.Builder;

@Builder
public record RegisterFoodCodeVo(
        String name,
        String code,
        String groupCode,
        Integer price,
        Double addWeightValue,
        Double addStrengthValue,
        Double addSatietyValue,
        Double addHealthyValue,
        Double addSleepValue
) {
}
