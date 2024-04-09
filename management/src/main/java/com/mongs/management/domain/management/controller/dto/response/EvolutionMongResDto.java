package com.mongs.management.domain.management.controller.dto.response;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record EvolutionMongResDto(
        Long mongId,
        String mongCode,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        String shiftCode,
        String stateCode,
        Double exp
) {
    public static EvolutionMongResDto of(MongVo mongVo) {
        return EvolutionMongResDto.builder()
                .mongId(mongVo.mongId())
                .mongCode(mongVo.mongCode())
                .weight(statusToPercent(mongVo.weight(), mongVo.grade()))
                .strength(statusToPercent(mongVo.strength(), mongVo.grade()))
                .satiety(statusToPercent(mongVo.satiety(), mongVo.grade()))
                .healthy(statusToPercent(mongVo.healthy(), mongVo.grade()))
                .sleep(statusToPercent(mongVo.sleep(), mongVo.grade()))
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .stateCode(mongVo.state().getCode())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
