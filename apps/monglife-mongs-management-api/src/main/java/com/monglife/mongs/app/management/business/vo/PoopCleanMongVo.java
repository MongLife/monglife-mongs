package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record PoopCleanMongVo(
        Long mongId,
        Integer poopCount,
        String shiftCode,

        Double exp,
        Double expPercent
) {
}
