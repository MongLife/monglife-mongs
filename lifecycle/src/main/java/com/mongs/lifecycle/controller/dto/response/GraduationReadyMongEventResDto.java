package com.mongs.lifecycle.controller.dto.response;

import lombok.Builder;

@Builder
public record GraduationReadyMongEventResDto(
        Long mongId
) {
}
