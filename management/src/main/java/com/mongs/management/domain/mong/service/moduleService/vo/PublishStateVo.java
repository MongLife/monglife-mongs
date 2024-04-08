package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

@Builder
public record PublishStateVo(
        Long mongId,
        String stateCode
) {
    public static PublishStateVo of(MongVo mongVo) {
        return PublishStateVo.builder()
                .mongId(mongVo.mongId())
                .stateCode(mongVo.state().getCode())
                .build();
    }
}
