package com.mongs.management.domain.mong.service.moduleService.vo;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishPoopVo(
        Long mongId,
        Integer poopCount,
        Double exp
) {
    public static PublishPoopVo of(MongVo mongVo) {
        return PublishPoopVo.builder()
                .mongId(mongVo.mongId())
                .poopCount(mongVo.poopCount())
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .build();
    }
}
