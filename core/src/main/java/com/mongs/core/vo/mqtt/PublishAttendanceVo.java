package com.mongs.core.vo.mqtt;

import lombok.Builder;

@Builder
public record PublishAttendanceVo(
        Long mongId,
        Double exp
) {
}