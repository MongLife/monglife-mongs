package com.mongs.play.app.management.internal.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import lombok.Builder;

@Builder
public record DecreaseStatusVo(
        Long accountId,
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep
) {
}
