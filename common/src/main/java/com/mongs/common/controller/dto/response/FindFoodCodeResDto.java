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
        Double addWeight,
        Double addStrength,
        Double addSatiety,
        Double addHealthy,
        Double addSleep,
        String buildVersion
) {
    public static FindFoodCodeResDto of(FoodCode foodCode) {
        return FindFoodCodeResDto.builder()
                .code(foodCode.code())
                .name(foodCode.name())
                .groupCode(foodCode.groupCode())
                .price(foodCode.price())
                .addWeight(foodCode.addWeightValue())
                .addStrength(foodCode.addStrengthValue())
                .addSatiety(foodCode.addSatietyValue())
                .addHealthy(foodCode.addHealthyValue())
                .addSleep(foodCode.addSleepValue())
                .buildVersion(foodCode.buildVersion())
                .build();
    }

    public static List<FindFoodCodeResDto> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFoodCodeResDto::of)
                .toList();
    }
}
