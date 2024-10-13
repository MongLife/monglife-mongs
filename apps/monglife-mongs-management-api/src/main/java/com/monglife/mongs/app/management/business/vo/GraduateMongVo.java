package com.monglife.mongs.app.management.business.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import lombok.Builder;

@Builder
public record GraduateMongVo(
        Long mongId,
        String shiftCode
) {
}
