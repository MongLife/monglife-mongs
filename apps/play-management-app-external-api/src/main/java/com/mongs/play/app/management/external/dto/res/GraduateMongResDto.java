package com.mongs.play.app.management.external.dto.res;

import lombok.Builder;

@Builder
public record GraduateMongResDto(
        Long accountId,
        Long mongId,
        String shiftCode
) {
}
