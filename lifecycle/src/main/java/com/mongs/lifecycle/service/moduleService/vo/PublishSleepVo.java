package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishSleepVo(
        Long mongId,
        Double sleep
) {
    public static PublishSleepVo of(Mong mong) {
        return PublishSleepVo.builder()
                .mongId(mong.getId())
                .sleep(statusToPercent(mong.getSleep(), mong.getGrade()))
                .build();
    }
}
