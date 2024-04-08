package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

@Builder
public record PublishEvolutionReadyVo(
        Long mongId,
        String shiftCode
) {
    public static PublishEvolutionReadyVo of(MongVo mongVo) {
        return PublishEvolutionReadyVo.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
