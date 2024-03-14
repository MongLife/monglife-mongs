package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishShiftVo(
        Long mongId,
        String shiftCode
) {
}
