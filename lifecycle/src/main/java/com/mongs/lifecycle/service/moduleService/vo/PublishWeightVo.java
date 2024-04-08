package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishWeightVo(
        Long mongId,
        Double weight
) {
    public static PublishWeightVo of(Mong mong) {
        return PublishWeightVo.builder()
                .mongId(mong.getId())
                .weight(statusToPercent(mong.getWeight(), mong.getGrade()))
                .build();
    }
}
