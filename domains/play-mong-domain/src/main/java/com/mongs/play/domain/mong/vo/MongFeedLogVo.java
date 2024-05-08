package com.mongs.play.domain.mong.vo;

import com.mongs.play.module.code.entity.FoodCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record MongFeedLogVo(
        String code,
        LocalDateTime lastBuyAt,
        Integer delaySeconds
) {
    public static MongFeedLogVo of(FoodCode foodCode) {
        return MongFeedLogVo.builder()
                .code(foodCode.code())
                .lastBuyAt(LocalDateTime.now().minusSeconds(foodCode.delaySeconds()))
                .delaySeconds(foodCode.delaySeconds())
                .build();
    }

    public static List<MongFeedLogVo> toList(List<FoodCode> foodCodeList) {
        return foodCodeList.stream()
                .map(MongFeedLogVo::of)
                .toList();
    }
}
