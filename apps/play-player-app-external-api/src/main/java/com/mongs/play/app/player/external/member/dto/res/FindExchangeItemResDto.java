package com.mongs.play.app.player.external.member.dto.res;

import com.mongs.play.app.player.external.member.vo.FindExchangeItemVo;
import lombok.Builder;

import java.util.List;

@Builder
public record FindExchangeItemResDto(
        String exchangeItemId,
        String name,
        Integer starPoint,
        Integer payPoint
) {
    public static FindExchangeItemResDto of(FindExchangeItemVo findExchangeItemVo) {
        return FindExchangeItemResDto.builder()
                .exchangeItemId(findExchangeItemVo.exchangeItemId())
                .name(findExchangeItemVo.name())
                .starPoint(findExchangeItemVo.starPoint())
                .payPoint(findExchangeItemVo.payPoint())
                .build();
    }

    public static List<FindExchangeItemResDto> toList(List<FindExchangeItemVo> findExchangeItemVoList) {
        return findExchangeItemVoList.stream()
                .map(FindExchangeItemResDto::of)
                .toList();
    }
}
