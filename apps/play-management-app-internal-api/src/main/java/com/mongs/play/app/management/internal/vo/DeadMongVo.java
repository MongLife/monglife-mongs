package com.mongs.play.app.management.internal.vo;

import lombok.Builder;

@Builder
public record DeadMongVo(
        Long mongId,
        String shiftCode,
        String stateCode,
        Integer poopCount,
        Boolean isSleeping,
        Double weight,

        Double exp,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double expPercent,
        Double strengthPercent,
        Double satietyPercent,
        Double healthyPercent,
        Double sleepPercent
) {
}
