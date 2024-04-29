package com.mongs.common.controller.dto.request;

import lombok.Builder;

@Builder
public record RegisterFoodCodeReqDto(
        String name,
        String code,
        String groupCode,
        Integer price,
        Double addWeightValue,
        Double addStrengthValue,
        Double addSatietyValue,
        Double addHealthyValue,
        Double addSleepValue,
        String buildVersion
) {
}
