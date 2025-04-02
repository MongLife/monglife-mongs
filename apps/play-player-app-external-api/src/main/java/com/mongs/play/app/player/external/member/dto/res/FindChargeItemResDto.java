package com.mongs.play.app.player.external.member.dto.res;

import com.mongs.play.app.player.external.member.vo.FindChargeItemVo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindChargeItemResDto(
        String chargeItemId,
        String name,
        Integer price,
        Integer starPoint
) {
    public static FindChargeItemResDto of(FindChargeItemVo findChargeItemVo) {
        return FindChargeItemResDto.builder()
                .chargeItemId(findChargeItemVo.chargeItemId())
                .name(findChargeItemVo.name())
                .price(findChargeItemVo.price())
                .starPoint(findChargeItemVo.starPoint())
                .build();
    }

    public static List<FindChargeItemResDto> toList(List<FindChargeItemVo> findChargeItemVoList) {
        return findChargeItemVoList.stream()
                .map(FindChargeItemResDto::of)
                .toList();
    }
}
