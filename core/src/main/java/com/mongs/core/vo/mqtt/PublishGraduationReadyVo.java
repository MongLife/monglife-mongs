package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishGraduationReadyVo(
        Long mongId,
        String shiftCode
) {
}
