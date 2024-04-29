package com.mongs.lifecycle.controller.dto.response;

import lombok.Builder;

@Builder
public record SleepMongEventResDto(
        Long mongId
) {
}
