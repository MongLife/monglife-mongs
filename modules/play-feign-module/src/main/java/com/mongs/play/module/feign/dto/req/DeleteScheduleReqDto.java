package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record DeleteScheduleReqDto(
        Long mongId
) {
}
