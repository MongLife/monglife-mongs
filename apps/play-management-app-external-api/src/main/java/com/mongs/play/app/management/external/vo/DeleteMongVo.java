package com.mongs.play.app.management.external.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import lombok.Builder;

@Builder
public record DeleteMongVo(
        Long accountId,
        Long mongId,
        MongShift shift
) {
}
