package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

@Builder
public record SleepingMongResDto(
        Long mongId,
        Boolean isSleeping
) {
    public static SleepingMongResDto of(MongVo mongVo) {
        return SleepingMongResDto.builder()
                .mongId(mongVo.mongId())
                .isSleeping(mongVo.isSleeping())
                .build();
    }
}
