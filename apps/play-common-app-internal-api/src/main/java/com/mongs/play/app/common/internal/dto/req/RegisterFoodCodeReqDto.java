package com.mongs.play.app.common.internal.dto.req;

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
        Integer delaySeconds,
        String buildVersion
) {
}
