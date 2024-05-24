package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record TrainingMongVo(
        Long mongId,
        Double weight,
        Integer payPoint,
        String shiftCode,

        Double exp,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double expPercent,
        Double healthyPercent,
        Double satietyPercent,
        Double strengthPercent,
        Double sleepPercent
) {
}
