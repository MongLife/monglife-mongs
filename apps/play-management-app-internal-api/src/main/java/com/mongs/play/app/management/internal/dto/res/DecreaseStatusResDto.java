package com.mongs.play.app.management.internal.dto.res;

import lombok.Builder;

@Builder
public record DecreaseStatusResDto(
        Long accountId,
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep
) {
}
