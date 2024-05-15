package com.mongs.play.app.management.external.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMongVo(
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
        Integer payPoint,
        LocalDateTime born
) {
    public static RegisterMongVo of(MongVo mongVo) {

        MongStatusPercentVo mongStatusPercentVo = MongUtil.statusToPercent(mongVo.grade(), mongVo);
        return RegisterMongVo.builder()
                .accountId(mongVo.accountId())
                .mongId(mongVo.mongId())
                .name(mongVo.name())
                .mongCode(mongVo.mongCode())
                .weight(mongVo.weight())
                .strength(mongStatusPercentVo.strength())
                .satiety(mongStatusPercentVo.satiety())
                .healthy(mongStatusPercentVo.healthy())
                .sleep(mongStatusPercentVo.sleep())
                .exp(mongStatusPercentVo.exp())
                .poopCount(mongVo.poopCount())
                .isSleeping(mongVo.isSleeping())
                .state(mongVo.state())
                .shift(mongVo.shift())
                .payPoint(mongVo.payPoint())
                .born(mongVo.born())
                .build();
    }
}
