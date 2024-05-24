package com.mongs.play.app.management.internal.dto.req;

import lombok.Builder;

@Builder
public record DeadScheduleReqDto(
        Long mongId
) {
}
