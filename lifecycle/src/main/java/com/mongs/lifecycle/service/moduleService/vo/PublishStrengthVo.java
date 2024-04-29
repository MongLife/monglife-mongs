package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishStrengthVo(
        Long mongId,
        Double strength
) {
    public static PublishStrengthVo of(Mong mong) {
        return PublishStrengthVo.builder()
                .mongId(mong.getId())
                .strength(statusToPercent(mong.getStrength(), mong.getGrade()))
                .build();
    }
}
