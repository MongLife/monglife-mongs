package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record StrokeMongResDto(
        Long accountId,
        Long mongId,
        Double exp
) {
}
