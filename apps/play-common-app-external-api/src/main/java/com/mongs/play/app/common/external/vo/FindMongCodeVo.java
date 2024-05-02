package com.mongs.play.app.common.external.vo;

import com.mongs.play.domain.code.entity.MongCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindMongCodeVo(
        String code,
        String name,
        String buildVersion
) {
    public static FindMongCodeVo of(MongCode mongCode) {
        return FindMongCodeVo.builder()
                .code(mongCode.code())
                .name(mongCode.name())
                .buildVersion(mongCode.buildVersion())
                .build();
    }

    public static List<FindMongCodeVo> toList(List<MongCode> mongCodeList) {
        return mongCodeList.stream()
                .map(FindMongCodeVo::of)
                .toList();
    }
}
