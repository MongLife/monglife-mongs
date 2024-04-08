package com.mongs.management.domain.mong.service.moduleService.vo;

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
    public static PublishCreateVo of(RegisterMongVo registerMongVo) {
        return PublishCreateVo.builder()
                .mongId(registerMongVo.mongId())
                .name(registerMongVo.name())
                .mongCode(registerMongVo.mongCode())
                .weight(statusToPercent(registerMongVo.weight(), registerMongVo.grade()))
                .strength(statusToPercent(registerMongVo.strength(), registerMongVo.grade()))
                .satiety(statusToPercent(registerMongVo.satiety(), registerMongVo.grade()))
                .healthy(statusToPercent(registerMongVo.healthy(), registerMongVo.grade()))
                .sleep(statusToPercent(registerMongVo.sleep(), registerMongVo.grade()))
                .exp(statusToPercent(registerMongVo.exp(), registerMongVo.grade()))
                .poopCount(registerMongVo.poopCount())
                .isSleeping(registerMongVo.isSleeping())
                .stateCode(registerMongVo.state().getCode())
                .shiftCode(registerMongVo.shift().getCode())
                .payPoint(registerMongVo.payPoint())
                .born(registerMongVo.born())
                .build();
    }
}
