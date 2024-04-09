package com.mongs.management.domain.feed.service.vo;

import com.mongs.core.enums.management.MongGrade;
import com.mongs.management.global.entity.Mong;
import lombok.Builder;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;

@Builder
public record FeedMongVo(
        Long accountId,
        Long mongId,
        Double weight,
        Double strength,
        Double satiety,
        Double healthy,
        Double sleep,
        Double exp,
        MongGrade grade,
        Integer payPoint
) {
    public static FeedMongVo of(Mong mong) {
        return FeedMongVo.builder()
                .accountId(mong.getAccountId())
                .mongId(mong.getId())
                .weight(statusToPercent(mong.getWeight(), mong.getGrade()))
                .strength(statusToPercent(mong.getStrength(), mong.getGrade()))
                .satiety(statusToPercent(mong.getSatiety(), mong.getGrade()))
                .healthy(statusToPercent(mong.getHealthy(), mong.getGrade()))
                .sleep(statusToPercent(mong.getSleep(), mong.getGrade()))
                .exp(statusToPercent((double) mong.getExp(), mong.getGrade()))
                .grade(mong.getGrade())
                .payPoint(mong.getPayPoint())
                .build();
    }
}
