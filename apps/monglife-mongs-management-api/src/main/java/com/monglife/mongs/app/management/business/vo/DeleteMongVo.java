package com.monglife.mongs.app.management.business.vo;

import lombok.Builder;

@Builder
public record DeleteMongVo(
        Long mongId,
        String shiftCode
) {
}
