package com.mongs.play.app.management.internal.vo;

import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import lombok.Builder;

@Builder
public record DeadMongVo(
        Long accountId,
        Long mongId,

        MongGrade grade,
        MongShift shift,
        MongState state,

        Double exp,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping
) {
}
