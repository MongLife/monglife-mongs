package com.mongs.management.domain.mong.service.componentService.vo;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.management.domain.mong.entity.Mong;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MongVo(
        Long accountId,
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
        MongState state,
        MongShift shift,
        MongGrade grade,
        Integer payPoint,
        LocalDateTime born
) {
    public static MongVo of(Mong mong) {
        return MongVo.builder()
                .accountId(mong.getAccountId())
                .mongId(mong.getId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .weight(mong.getWeight())
                .strength(mong.getStrength())
                .satiety(mong.getSatiety())
                .healthy(mong.getHealthy())
                .sleep(mong.getSleep())
                .exp((double) mong.getExp())
                .poopCount(mong.getNumberOfPoop())
                .isSleeping(mong.getIsSleeping())
                .state(mong.getState())
                .shift(mong.getShift())
                .grade(mong.getGrade())
                .payPoint(mong.getPayPoint())
                .born(mong.getCreatedAt())
                .build();
    }

    public static List<MongVo> toList(List<Mong> mongList) {
        return mongList.stream()
                .map(com.mongs.management.domain.mong.service.componentService.vo.MongVo::of)
                .toList();
    }
}
