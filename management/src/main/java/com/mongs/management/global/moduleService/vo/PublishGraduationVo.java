package com.mongs.management.global.moduleService.vo;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

@Builder
public record PublishGraduationVo(
        Long mongId,
        String shiftCode
) {
    public static PublishGraduationVo of(MongVo mongVo) {
        return PublishGraduationVo.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
