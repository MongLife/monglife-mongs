package com.mongs.play.module.feign.dto.req;

import lombok.Builder;

@Builder
public record DeadScheduleReqDto(
        Long mongId
) {
}
