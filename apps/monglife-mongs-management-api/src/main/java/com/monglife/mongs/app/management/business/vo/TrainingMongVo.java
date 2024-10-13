package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record TrainingMongVo(
        Long mongId,
        Double weight,
        Integer payPoint,
        String stateCode,
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
        Double sleepPercent,
        Boolean isDeadSchedule
) {
}
