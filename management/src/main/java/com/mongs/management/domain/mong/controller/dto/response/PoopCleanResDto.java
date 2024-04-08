package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PoopCleanResDto(
        Long mongId,
        Integer poopCount,
        Double exp
) {
    public static PoopCleanResDto of(MongVo mongVo) {
        return PoopCleanResDto.builder()
                .mongId(mongVo.mongId())
                .poopCount(mongVo.poopCount())
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .build();
    }
}
