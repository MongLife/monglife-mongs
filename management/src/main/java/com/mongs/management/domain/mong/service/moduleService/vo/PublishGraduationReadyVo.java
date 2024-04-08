package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
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
