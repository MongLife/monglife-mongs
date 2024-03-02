package com.mongs.common.dto.response;

import com.mongs.core.code.entity.MongCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindMongCodeResDto(
        String code,
        String name
) {
    public static FindMongCodeResDto of(MongCode mongCode) {
        return FindMongCodeResDto.builder()
                .code(mongCode.code())
                .name(mongCode.name())
                .build();
    }

    public static List<FindMongCodeResDto> toList(List<MongCode> mongCodeList) {
        return mongCodeList.stream()
                .map(FindMongCodeResDto::of)
                .toList();
    }
}
