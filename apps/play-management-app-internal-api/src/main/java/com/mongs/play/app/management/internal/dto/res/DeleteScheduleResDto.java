package com.mongs.play.app.management.internal.dto.res;

import lombok.Builder;

@Builder
public record DeleteScheduleResDto(
        Long mongId
) {
}
