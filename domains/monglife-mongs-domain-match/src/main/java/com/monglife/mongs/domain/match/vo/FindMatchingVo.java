package com.monglife.mongs.domain.match.vo;

import com.monglife.mongs.domain.match.entity.MatchingEntity;
import lombok.*;

@Getter
public class FindMatchingVo {

    private final Long mongId;

    private final String deviceId;

    private final Long accountId;

    private final Boolean isBot;

    @Builder
    public FindMatchingVo(Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }

    public static FindMatchingVo of(MatchingEntity matchingEntity) {
        return FindMatchingVo.builder()
                .mongId(matchingEntity.getMongId())
                .deviceId(matchingEntity.getDeviceId())
                .accountId(matchingEntity.getAccountId())
                .isBot(matchingEntity.getIsBot())
                .build();
    }
}
