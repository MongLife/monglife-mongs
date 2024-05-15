package com.mongs.play.app.management.external.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindMongVo(
        Long accountId,
        Long mongId,
        String name,
        String mongCode,
        Integer payPoint,

        MongState state,
        MongShift shift,

        Double exp,
        Double weight,
        Double healthy,
        Double satiety,
        Double strength,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping,

        LocalDateTime born
) {
}
