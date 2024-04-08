package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

@Builder
public record PublishSleepingVo(
        Long mongId,
        Boolean isSleeping
) {
    public static PublishSleepingVo of(MongVo mongVo) {
        return PublishSleepingVo.builder()
                .mongId(mongVo.mongId())
                .isSleeping(mongVo.isSleeping())
                .build();
    }
}
