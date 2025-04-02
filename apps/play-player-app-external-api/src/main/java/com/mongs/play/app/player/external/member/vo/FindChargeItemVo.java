package com.mongs.play.app.player.external.member.vo;

import com.mongs.play.domain.payment.entity.ChargeItem;
import lombok.Builder;

import java.util.List;

@Builder
public record FindChargeItemVo(
        String chargeItemId,
        String name,
        Integer price,
        Integer starPoint
) {
    public static FindChargeItemVo of(ChargeItem chargeItem) {
        return FindChargeItemVo.builder()
                .chargeItemId(chargeItem.getChargeItemId())
                .name(chargeItem.getName())
                .price(chargeItem.getPrice())
                .starPoint(chargeItem.getStarPoint())
                .build();
    }

    public static List<FindChargeItemVo> toList(List<ChargeItem> chargeItemList) {
        return chargeItemList.stream()
                .map(FindChargeItemVo::of)
                .toList();
    }
}
