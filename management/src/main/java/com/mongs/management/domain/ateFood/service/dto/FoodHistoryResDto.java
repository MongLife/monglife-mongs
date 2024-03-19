package com.mongs.management.domain.ateFood.service.dto;
import com.mongs.core.entity.FoodCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record FoodHistoryResDto(
        String code,
        Integer price,
        Double addWeightValue,
        Double addStrengthValue,
        Double addSatietyValue,
        Double addHealthyValue,
        Double addSleepValue,
        LocalDateTime lastBuyAt
) {
    public static FoodHistoryResDto of(FoodCode foodCode) {
        return FoodHistoryResDto.builder()
                .code(foodCode.code())
                .price(foodCode.price())
                .addWeightValue(foodCode.addWeightValue())
                .addStrengthValue(foodCode.addStrengthValue())
                .addSatietyValue(foodCode.addSatietyValue())
                .addHealthyValue(foodCode.addHealthyValue())
                .addSleepValue(foodCode.addSleepValue())
                .build();
    }

    public static List<FoodHistoryResDto> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FoodHistoryResDto::of)
                .toList();
    }
}
