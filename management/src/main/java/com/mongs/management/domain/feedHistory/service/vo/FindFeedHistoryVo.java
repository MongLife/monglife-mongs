package com.mongs.management.domain.feedHistory.service.vo;
import com.mongs.core.entity.FoodCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record FindFeedHistoryVo(
        String code,
        LocalDateTime lastBuyAt
) {
    public static FindFeedHistoryVo of(FoodCode foodCode) {
        return FindFeedHistoryVo.builder()
                .code(foodCode.code())
                .build();
    }

    public static List<FindFeedHistoryVo> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(FindFeedHistoryVo::of)
                .toList();
    }
}