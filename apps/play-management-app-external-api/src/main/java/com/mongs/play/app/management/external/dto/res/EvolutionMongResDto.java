package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record EvolutionMongResDto(
        Long accountId,
        Long mongId,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp,
        String shiftCode,
        String stateCode
) {
}
