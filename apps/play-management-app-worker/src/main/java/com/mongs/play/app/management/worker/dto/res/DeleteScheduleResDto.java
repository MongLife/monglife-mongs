package com.mongs.play.app.management.worker.dto.res;

import lombok.Builder;

@Builder
public record DeleteScheduleResDto(
        Long mongId
) {
}