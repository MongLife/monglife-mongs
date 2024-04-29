package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

@Builder
public record PublishDeadVo(
        Long mongId,
        String shiftCode
) {
    public static PublishDeadVo of(Mong mong) {
        return PublishDeadVo.builder()
                .mongId(mong.getId())
                .shiftCode(mong.getShift().getCode())
                .build();
    }
}
