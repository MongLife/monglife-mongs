package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record EvolutionMongResDto(
        Long mongId,
        String mongCode,
        String shiftCode,
        String stateCode,
        Integer exp
) {
}
