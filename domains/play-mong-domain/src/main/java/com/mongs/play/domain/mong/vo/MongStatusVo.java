package com.mongs.play.domain.mong.vo;

import com.mongs.play.domain.mong.enums.MongGrade;
import lombok.Builder;

@Builder
public record MongStatusVo(
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp
) {
}
