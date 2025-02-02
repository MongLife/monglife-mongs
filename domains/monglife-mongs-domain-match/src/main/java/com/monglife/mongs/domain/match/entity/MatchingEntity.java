package com.monglife.mongs.domain.match.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MatchingEntity {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private Boolean isBot;

    @Builder
    public MatchingEntity(Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }
}
