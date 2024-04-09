package com.mongs.management.global.moduleService.vo;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

@Builder
public record PublishGraduationReadyVo(
        Long mongId,
        String shiftCode
) {
    public static PublishGraduationReadyVo of(MongVo mongVo) {
        return PublishGraduationReadyVo.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
