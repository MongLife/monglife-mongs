package com.monglife.mongs.app.management.business.vo;

import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindMongVo(
        Long mongId,
        String mongCode,
        Double weight,
        String name,
        Integer payPoint,
        String shiftCode,
        String stateCode,
        Integer poopCount,
        Boolean isSleeping,
        LocalDateTime born,

        Double exp,
        Double healthy,
        Double satiety,
        Double strength,
        Double sleep,
        Double expPercent,
        Double healthyPercent,
        Double satietyPercent,
        Double strengthPercent,
        Double sleepPercent
) {
}
