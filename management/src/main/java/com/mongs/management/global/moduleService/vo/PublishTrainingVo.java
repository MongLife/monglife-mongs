package com.mongs.management.global.moduleService.vo;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishTrainingVo(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp,
        Integer payPoint
) {
    public static PublishTrainingVo of(MongVo mongVo) {
        return PublishTrainingVo.builder()
                .mongId(mongVo.mongId())
                .weight(statusToPercent(mongVo.weight(), mongVo.grade()))
                .strength(statusToPercent(mongVo.strength(), mongVo.grade()))
                .satiety(statusToPercent(mongVo.satiety(), mongVo.grade()))
                .healthy(statusToPercent(mongVo.healthy(), mongVo.grade()))
                .sleep(statusToPercent(mongVo.sleep(), mongVo.grade()))
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .payPoint(mongVo.payPoint())
                .build();

    }
}
