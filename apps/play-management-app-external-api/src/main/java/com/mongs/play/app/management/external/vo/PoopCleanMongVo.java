package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record PoopCleanMongVo(
        Long accountId,
        Long mongId,
        Integer poopCount,
        Double exp
) {
}