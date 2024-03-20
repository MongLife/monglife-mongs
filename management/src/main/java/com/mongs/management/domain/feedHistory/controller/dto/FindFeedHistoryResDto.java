package com.mongs.management.domain.feedHistory.controller.dto;
import com.mongs.management.domain.feedHistory.service.vo.FindFeedHistoryVo;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record FindFeedHistoryResDto(
        String code,
        LocalDateTime lastBuyAt
) {
    public static FindFeedHistoryResDto of(FindFeedHistoryVo findFeedHistoryVo) {
        return FindFeedHistoryResDto.builder()
                .code(findFeedHistoryVo.code())
                .lastBuyAt(findFeedHistoryVo.lastBuyAt())
                .build();
    }
}
