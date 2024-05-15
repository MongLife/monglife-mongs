package com.mongs.play.app.management.external.dto.res;

import com.mongs.play.app.management.external.vo.StrokeMongVo;
import lombok.Builder;

@Builder
public record StrokeMongResDto(
        Long accountId,
        Long mongId,
        Double exp
) {
}
