package com.mongs.common.controller.dto.response;

import com.mongs.core.entity.FoodCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFoodCodeResDto(
        String code,
        String name,
        String groupCode,
        Integer point
) {
    public static FindFoodCodeResDto of(FoodCode mongCode) {
        return FindFoodCodeResDto.builder()
                .code(mongCode.code())
                .name(mongCode.name())
                .groupCode(mongCode.groupCode())
                .point(mongCode.price())
                .build();
    }

    public static List<FindFoodCodeResDto> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFoodCodeResDto::of)
                .toList();
    }
}
