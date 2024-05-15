package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record StrokeMongVo(
        Long accountId,
        Long mongId,
        Double exp
) {
}
