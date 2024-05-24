package com.mongs.play.app.common.external.vo;

import com.mongs.play.domain.code.entity.MapCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindMapCodeVo(
        String code,
        String name,
        String buildVersion
) {
    public static FindMapCodeVo of(MapCode mapCode) {
        return FindMapCodeVo.builder()
                .code(mapCode.getCode())
                .name(mapCode.getName())
                .buildVersion(mapCode.getBuildVersion())
                .build();
    }

    public static List<FindMapCodeVo> toList(List<MapCode> mapCodeList) {
        return mapCodeList.stream()
                .map(FindMapCodeVo::of)
                .toList();
    }
}
