package com.mongs.play.app.management.external.dto.res;

import com.mongs.play.app.management.external.vo.DeleteMongVo;
import lombok.Builder;

@Builder
public record DeleteMongResDto(
        Long accountId,
        Long mongId,
        String shiftCode
) {
}
