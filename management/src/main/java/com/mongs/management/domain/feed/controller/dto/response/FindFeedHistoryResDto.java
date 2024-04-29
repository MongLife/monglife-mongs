package com.mongs.management.domain.feed.controller.dto.response;
import com.mongs.management.domain.feed.service.vo.FindFeedHistoryVo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record FindFeedHistoryResDto(
        String code,
        Boolean isCanBuy
) {
    public static FindFeedHistoryResDto of(FindFeedHistoryVo findFeedHistoryVo) {
        return FindFeedHistoryResDto.builder()
                .code(findFeedHistoryVo.code())
                .isCanBuy(findFeedHistoryVo.lastBuyAt().plusSeconds(findFeedHistoryVo.delaySeconds()).isBefore(LocalDateTime.now()))
                .build();
    }
}
