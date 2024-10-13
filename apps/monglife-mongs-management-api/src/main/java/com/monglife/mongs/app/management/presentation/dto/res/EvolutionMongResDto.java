package com.monglife.mongs.app.management.presentation.dto.res;

import lombok.Builder;

@Builder
public record EvolutionMongResDto(
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
