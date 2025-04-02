package com.mongs.play.module.feign.dto.res;

import lombok.Builder;

@Builder
public record DeleteScheduleResDto(
        Long mongId
) {
}
