package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishSatietyVo(
        Long mongId,
        Double satiety
) {
    public static PublishSatietyVo of(Mong mong) {
        return PublishSatietyVo.builder()
                .mongId(mong.getId())
                .satiety(statusToPercent(mong.getSatiety(), mong.getGrade()))
                .build();
    }
}
