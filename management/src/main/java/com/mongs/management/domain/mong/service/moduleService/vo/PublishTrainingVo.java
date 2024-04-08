package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishTrainingVo(
        Long mongId,
        Double strength,
        Double exp,
        Integer payPoint
) {
    public static PublishTrainingVo of(MongVo mongVo) {
        return PublishTrainingVo.builder()
                .mongId(mongVo.mongId())
                .strength(statusToPercent(mongVo.strength(), mongVo.grade()))
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .payPoint(mongVo.payPoint())
                .build();

    }
}
