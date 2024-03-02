package com.mongs.common.dto.response;

import com.mongs.core.code.entity.FoodCode;
import com.mongs.core.code.entity.MongCode;
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
                .point(mongCode.point())
                .build();
    }

    public static List<FindFoodCodeResDto> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFoodCodeResDto::of)
                .toList();
    }
}
