package com.monglife.mongs.domain.battle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class QueuePlayer {
    // 봇 매칭 대기 시간 (초)
    private static final Long EXPIRED_SECONDS = 10L;

    private final Long mongId;

    private final String deviceId;

    private final Long accountId;

    @Builder
    public QueuePlayer(Long mongId, String deviceId, Long accountId) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
    }

    /**
     * 봇 매칭 대기 시간 (초)
     * @return 봇 매칭 대기 시간
     */
    public static Long getExpiredSeconds() {
        return EXPIRED_SECONDS;
    }
}
