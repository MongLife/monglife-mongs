package com.mongs.management.domain.mong.controller.dto.response;

import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import lombok.Builder;

@Builder
public record GraduateMongResDto(
        Long mongId,
        String shiftCode
) {
    public static GraduateMongResDto of(MongVo mongVo) {
        return GraduateMongResDto.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
