package com.mongs.management.domain.mong.controller.dto.response;

import lombok.Builder;

@Builder
public record GraduateMongResDto(
        Long mongId,
        String shiftCode
) {
}
