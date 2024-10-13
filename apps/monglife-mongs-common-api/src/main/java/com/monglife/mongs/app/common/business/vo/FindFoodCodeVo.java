package com.monglife.mongs.app.common.business.vo;

import com.mongs.play.domain.code.entity.FoodCode;
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
                .code(foodCode.getCode())
                .name(foodCode.getName())
                .groupCode(foodCode.getGroupCode())
                .price(foodCode.getPrice())
                .addWeight(foodCode.getAddWeightValue())
                .addStrength(foodCode.getAddStrengthValue())
                .addSatiety(foodCode.getAddSatietyValue())
                .addHealthy(foodCode.getAddHealthyValue())
                .addSleep(foodCode.getAddSleepValue())
                .buildVersion(foodCode.getBuildVersion())
                .build();
    }

    public static List<FindFoodCodeVo> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFoodCodeVo::of)
                .toList();
    }
}
