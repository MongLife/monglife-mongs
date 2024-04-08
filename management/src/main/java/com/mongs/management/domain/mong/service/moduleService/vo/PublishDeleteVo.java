package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

@Builder
public record PublishDeleteVo(
        Long mongId,
        String shiftCode
) {
    public static PublishDeleteVo of(MongVo mongVo) {
        return PublishDeleteVo.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
