package com.mongs.management.domain.management.controller.dto.response;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record TrainingMongResDto(
        Long mongId,
        Double strength,
        Double exp,
        Integer payPoint
) {
    public static TrainingMongResDto of(MongVo mongVo) {
        return TrainingMongResDto.builder()
                .mongId(mongVo.mongId())
                .strength(statusToPercent(mongVo.strength(), mongVo.grade()))
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .payPoint(mongVo.payPoint())
                .build();
    }
}
