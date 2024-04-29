package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

@Builder
public record PublishEvolutionReadyVo(
        Long mongId,
        String shiftCode
) {
    public static PublishEvolutionReadyVo of(Mong mong) {
        return PublishEvolutionReadyVo.builder()
                .mongId(mong.getId())
                .shiftCode(mong.getShift().getCode())
                .build();
    }
}
