package com.mongs.play.app.player.external.member.vo;

import com.mongs.play.domain.payment.entity.ExchangeItem;
import lombok.Builder;

import java.util.List;

@Builder
public record FindExchangeItemVo(
        String exchangeItemId,
        String name,
        Integer starPoint,
        Integer payPoint
) {
    public static FindExchangeItemVo of(ExchangeItem exchangeItem) {
        return FindExchangeItemVo.builder()
                .exchangeItemId(exchangeItem.getExchangeItemId())
                .name(exchangeItem.getName())
                .starPoint(exchangeItem.getStarPoint())
                .payPoint(exchangeItem.getPayPoint())
                .build();
    }

    public static List<FindExchangeItemVo> toList(List<ExchangeItem> exchangeItemList) {
        return exchangeItemList.stream()
                .map(FindExchangeItemVo::of)
                .toList();
    }
}
