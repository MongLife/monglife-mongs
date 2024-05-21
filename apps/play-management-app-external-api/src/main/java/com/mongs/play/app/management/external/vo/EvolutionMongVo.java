package com.mongs.play.app.management.external.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import lombok.Builder;

@Builder
public record EvolutionMongVo(
        Long mongId,
        String mongCode,
        Double weight,

        String shiftCode,
        String stateCode,

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
