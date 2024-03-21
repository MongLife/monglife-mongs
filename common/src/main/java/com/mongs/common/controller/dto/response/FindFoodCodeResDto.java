package com.mongs.common.controller.dto.response;

import com.mongs.core.entity.FoodCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFoodCodeResDto(
        String code,
        String name,
        String groupCode,
        Integer price,
        Long version
) {
    public static FindFoodCodeResDto of(FoodCode foodCode) {
        return FindFoodCodeResDto.builder()
                .code(foodCode.code())
                .name(foodCode.name())
                .groupCode(foodCode.groupCode())
                .price(foodCode.price())
                .version(foodCode.version())
                .build();
    }

    public static List<FindFoodCodeResDto> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFoodCodeResDto::of)
                .toList();
    }
}
