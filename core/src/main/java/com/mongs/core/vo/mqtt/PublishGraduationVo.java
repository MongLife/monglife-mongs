package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishGraduationVo(
        Long mongId,
        String shiftCode
) {
}
