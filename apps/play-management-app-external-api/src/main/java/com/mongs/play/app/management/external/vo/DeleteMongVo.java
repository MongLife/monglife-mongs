package com.mongs.play.app.management.external.vo;

import lombok.Builder;

@Builder
public record DeleteMongVo(
        Long mongId,
        String shiftCode
) {
}
