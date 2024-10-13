package com.monglife.mongs.app.management.business.vo;

import com.mongs.play.domain.mong.enums.MongShift;
import com.mongs.play.domain.mong.enums.MongState;
import com.mongs.play.domain.mong.utils.MongUtil;
import com.mongs.play.domain.mong.vo.MongStatusPercentVo;
import com.mongs.play.domain.mong.vo.MongVo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterMongVo(
        Long mongId,
        String name,
        String mongCode,
        Double weight,
        Integer poopCount,
        Boolean isSleeping,
        String shiftCode,
        String stateCode,
        Integer payPoint,
        LocalDateTime born,

        Double exp,
        Double healthy,
        Double satiety,
        Double strength,
        Double sleep,
        Double expPercent,
        Double healthyPercent,
        Double satietyPercent,
        Double strengthPercent,
        Double sleepPercent
) {
}
