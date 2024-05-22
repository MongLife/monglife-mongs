package com.mongs.play.app.management.worker.dto.req;

import lombok.Builder;

@Builder
public record DeadScheduleReqDto(
        Long mongId
) {
}
