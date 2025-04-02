package com.mongs.play.domain.mong.vo;

import com.mongs.play.domain.mong.entity.Mong;
import com.mongs.play.domain.mong.enums.MongGrade;
import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import com.mongs.play.domain.mong.utils.MongUtil;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record MongVo(
        Boolean isActive,
        Long mongId,
        Long accountId,
        String name,
        String mongCode,
        Integer payPoint,

        MongGrade grade,
        MongShift shift,
        MongState state,

        String shiftCode,
        String stateCode,

        Double weight,
        Double exp,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,

        Double expPercent,
        Double healthyPercent,
        Double satietyPercent,
        Double strengthPercent,
        Double sleepPercent,

        Integer poopCount,
        Boolean isSleeping,

        Integer numberOfTraining,
        Integer numberOfStroke,
        Boolean isDeadSchedule,

        LocalDateTime born,

        Integer evolutionPoint,
        Integer penalty
) {
    public static MongVo of(Mong mong) {
        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mong.getGrade(), mong);

        return MongVo.builder()
                .isActive(mong.getIsActive())
                .mongId(mong.getId())
                .accountId(mong.getAccountId())
                .name(mong.getName())
                .mongCode(mong.getMongCode())
                .payPoint(mong.getPayPoint())
                .grade(mong.getGrade())
                .shift(mong.getShift())
                .state(mong.getState())
                .shiftCode(mong.getShift().code)
                .stateCode(mong.getState().code)
                .weight(mong.getWeight())
                .exp(mong.getExp())
                .strength(mong.getStrength())
                .satiety(mong.getSatiety())
                .healthy(mong.getHealthy())
                .sleep(mong.getSleep())
                .expPercent(mongStatusPercentVo.exp())
                .strengthPercent(mongStatusPercentVo.strength())
                .satietyPercent(mongStatusPercentVo.satiety())
                .healthyPercent(mongStatusPercentVo.healthy())
                .sleepPercent(mongStatusPercentVo.sleep())
                .poopCount(mong.getPoopCount())
                .isSleeping(mong.getIsSleeping())
                .numberOfTraining(mong.getNumberOfTraining())
                .numberOfStroke(mong.getNumberOfStroke())
                .isDeadSchedule(mong.getIsDeadSchedule())
                .born(mong.getCreatedAt())
                .evolutionPoint(mong.getEvolutionPoint())
                .penalty(mong.getPenalty())
                .build();
    }

    public static List<MongVo> toList(List<Mong> mongList) {
        return mongList.stream()
                .map(MongVo::of)
                .toList();
    }
}