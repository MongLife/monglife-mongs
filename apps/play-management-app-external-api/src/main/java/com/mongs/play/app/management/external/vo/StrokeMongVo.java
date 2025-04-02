package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record StrokeMongVo(
        Long mongId,
        String shiftCode,
        Double exp,
        Double expPercent
) {
}
