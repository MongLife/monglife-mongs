package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record EvolutionMongResDto(
        Long mongId,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double health,
        Double sleep,
        String shiftCode,
        String stateCode,
        Double exp
) {
}
