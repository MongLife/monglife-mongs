package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishHealthyVo(
        Long mongId,
        Double healthy
) {
    public static PublishHealthyVo of(Mong mong) {
        return PublishHealthyVo.builder()
                .mongId(mong.getId())
                .healthy(statusToPercent(mong.getHealthy(), mong.getGrade()))
                .build();
    }
}
