package com.mongs.management.domain.management.controller.dto.response;

import com.mongs.management.domain.management.service.vo.MongVo;
import lombok.Builder;

@Builder
public record DeleteMongResDto(
        Long mongId,
        String shiftCode
) {
    public static DeleteMongResDto of(MongVo mongVo) {
        return DeleteMongResDto.builder()
                .mongId(mongVo.mongId())
                .shiftCode(mongVo.shift().getCode())
                .build();
    }
}
