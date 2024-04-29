package com.mongs.management.global.moduleService.vo;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record PublishAttendanceVo(
        Long mongId,
        Double exp
) {
    public static PublishAttendanceVo of(MongVo mongVo) {
        return PublishAttendanceVo.builder()
                .mongId(mongVo.mongId())
                .exp(statusToPercent(mongVo.exp(), mongVo.grade()))
                .build();
    }
}
