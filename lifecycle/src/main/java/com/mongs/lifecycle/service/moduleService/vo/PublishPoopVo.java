package com.mongs.lifecycle.service.moduleService.vo;

import com.mongs.lifecycle.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishPoopVo(
        Long mongId,
        Integer poopCount,
        Double exp
) {
    public static PublishPoopVo of(Mong mong) {
        return PublishPoopVo.builder()
                .mongId(mong.getId())
                .poopCount(mong.getNumberOfPoop())
                .exp(statusToPercent((double) mong.getExp(), mong.getGrade()))
                .build();
    }
}
