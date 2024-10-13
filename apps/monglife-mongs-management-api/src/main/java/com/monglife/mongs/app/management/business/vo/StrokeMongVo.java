package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record StrokeMongVo(
        Long mongId,
        String shiftCode,
        Double exp,
        Double expPercent
) {
}
