package com.mongs.common.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FindCodeResDto(
        String version,
        List<FindMapCodeResDto> mapCodeList,
        List<FindMongCodeResDto> mongCodeList,
        List<FindFoodCodeResDto> foodCodeList
) {
}
