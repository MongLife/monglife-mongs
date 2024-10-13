package com.monglife.mongs.app.management.presentation.dto.res;

import lombok.Builder;

@Builder
public record StrokeMongResDto(
        Long mongId,
        Double exp
) {
}
