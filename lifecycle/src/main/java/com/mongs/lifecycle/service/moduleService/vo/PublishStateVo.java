package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

@Builder
public record PublishStateVo(
        Long mongId,
        String stateCode
) {
    public static PublishStateVo of(Mong mong) {
        return PublishStateVo.builder()
                .mongId(mong.getId())
                .stateCode(mong.getState().getCode())
                .build();
    }
}
