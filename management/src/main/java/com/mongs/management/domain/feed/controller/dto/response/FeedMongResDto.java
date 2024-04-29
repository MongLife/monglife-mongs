package com.mongs.management.domain.feed.controller.dto.response;

import com.mongs.management.domain.feed.service.vo.FeedMongVo;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record FeedMongResDto(
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp,
        Integer payPoint
) {
    public static FeedMongResDto of(FeedMongVo feedMongVo) {
        return FeedMongResDto.builder()
                .mongId(feedMongVo.mongId())
                .weight(statusToPercent(feedMongVo.weight(), feedMongVo.grade()))
                .strength(statusToPercent(feedMongVo.strength(), feedMongVo.grade()))
                .satiety(statusToPercent(feedMongVo.satiety(), feedMongVo.grade()))
                .healthy(statusToPercent(feedMongVo.healthy(), feedMongVo.grade()))
                .sleep(statusToPercent(feedMongVo.sleep(), feedMongVo.grade()))
                .exp(statusToPercent(feedMongVo.exp(), feedMongVo.grade()))
                .payPoint(feedMongVo.payPoint())
                .build();
    }
}
