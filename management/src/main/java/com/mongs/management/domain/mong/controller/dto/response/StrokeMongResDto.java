package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record StrokeMongResDto(
        Long mongId,
        Double exp
) {
    public static StrokeMongResDto of(MongVo mongVo) {
        return StrokeMongResDto.builder()
                .mongId(mongVo.mongId())
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .build();
    }
}
