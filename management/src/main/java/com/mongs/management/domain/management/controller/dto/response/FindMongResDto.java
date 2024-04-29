package com.mongs.management.domain.management.controller.dto.response;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record FindMongResDto(
        Long mongId,
        String name,
        String mongCode,
        Double weight,
        Double healthy,
        Double satiety,
        Double strength,
        Double sleep,
        Integer poopCount,
        Boolean isSleeping,
        Double exp,
        String stateCode,
        String shiftCode,
        Integer payPoint,
        LocalDateTime born
) {
    public static FindMongResDto of(MongVo mongVo) {
        return FindMongResDto.builder()
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

    public static List<FindMongResDto> toList(List<MongVo> mongVoList) {
        return mongVoList.stream()
                .map(FindMongResDto::of)
                .toList();
    }
}
