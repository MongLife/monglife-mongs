package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record GraduateReadyMongResDto(
        Long mongId,
        String shiftCode
) {
}
