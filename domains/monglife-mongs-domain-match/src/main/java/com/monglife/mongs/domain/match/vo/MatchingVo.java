package com.monglife.mongs.domain.match.vo;

import com.monglife.mongs.domain.match.entity.MatchingEntity;
import lombok.*;

@ToString
@Getter
public class MatchingVo {

    private final Long mongId;

    private final String deviceId;

    private final Long accountId;

    private final Boolean isBot;

    @Builder
    public MatchingVo(Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }

    public static MatchingVo of(MatchingEntity matchingEntity) {
        return MatchingVo.builder()
                .mongId(matchingEntity.getMongId())
                .deviceId(matchingEntity.getDeviceId())
                .accountId(matchingEntity.getAccountId())
                .isBot(matchingEntity.getIsBot())
                .build();
    }
}
