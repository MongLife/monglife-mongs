package com.mongs.common.controller.dto.response;

import com.mongs.core.entity.MapCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindMapCodeResDto(
        String code,
        String name,
        String buildVersion
) {
    public static FindMapCodeResDto of(MapCode mapCode) {
        return FindMapCodeResDto.builder()
                .code(mapCode.code())
                .name(mapCode.name())
                .buildVersion(mapCode.buildVersion())
                .build();
    }

    public static List<FindMapCodeResDto> toList(List<MapCode> mapCodeList) {
        return mapCodeList.stream()
                .map(FindMapCodeResDto::of)
                .toList();
    }
}
