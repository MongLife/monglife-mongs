package com.mongs.management.global.moduleService.vo;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishEvolutionVo(
        Long mongId,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        String stateCode,
        String shiftCode,
        Double exp
) {
    public static PublishEvolutionVo of(MongVo mongVo) {
        return PublishEvolutionVo.builder()
                .mongId(mongVo.mongId())
                .mongCode(mongVo.mongCode())
                .weight(statusToPercent(mongVo.weight(), mongVo.grade()))
                .strength(statusToPercent(mongVo.strength(), mongVo.grade()))
                .satiety(statusToPercent(mongVo.satiety(), mongVo.grade()))
                .healthy(statusToPercent(mongVo.healthy(), mongVo.grade()))
                .sleep(statusToPercent(mongVo.sleep(), mongVo.grade()))
                .stateCode(mongVo.state().getCode())
                .shiftCode(mongVo.shift().getCode())
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .build();
    }
}
