package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishStrokeVo(
        Long mongId,
        Double exp
) {
    public static PublishStrokeVo of(MongVo mongVo) {
        return PublishStrokeVo.builder()
                .mongId(mongVo.mongId())
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .build();
    }
}
