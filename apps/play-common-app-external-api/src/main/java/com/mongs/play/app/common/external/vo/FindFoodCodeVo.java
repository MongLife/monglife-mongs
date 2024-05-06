package com.mongs.play.app.common.external.vo;

import com.mongs.play.module.code.entity.FoodCode;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFoodCodeVo(
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
    public static FindFoodCodeVo of(FoodCode foodCode) {
        return FindFoodCodeVo.builder()
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

    public static List<FindFoodCodeVo> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFoodCodeVo::of)
                .toList();
    }
}
