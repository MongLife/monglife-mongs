package com.mongs.play.app.management.external.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import lombok.Builder;

@Builder
public record GraduateMongVo(
        Long mongId,
        String shiftCode
) {
}
