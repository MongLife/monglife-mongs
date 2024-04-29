package com.mongs.management.global.moduleService.vo;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import java.time.LocalDateTime;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishCreateVo(
        Long mongId,
        String name,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping,
        Double exp,
        String stateCode,
        String shiftCode,
        Integer payPoint,
        LocalDateTime born
) {
    public static PublishCreateVo of(MongVo mongVo) {
        return PublishCreateVo.builder()
                .mongId(mongVo.mongId())
                .name(mongVo.name())
                .mongCode(mongVo.mongCode())
                .weight(statusToPercent(mongVo.weight(), mongVo.grade()))
                .strength(statusToPercent(mongVo.strength(), mongVo.grade()))
                .satiety(statusToPercent(mongVo.satiety(), mongVo.grade()))
                .healthy(statusToPercent(mongVo.healthy(), mongVo.grade()))
                .sleep(statusToPercent(mongVo.sleep(), mongVo.grade()))
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .poopCount(mongVo.poopCount())
                .isSleeping(mongVo.isSleeping())
                .stateCode(mongVo.state().getCode())
                .shiftCode(mongVo.shift().getCode())
                .payPoint(mongVo.payPoint())
                .born(mongVo.born())
                .build();
    }
}
