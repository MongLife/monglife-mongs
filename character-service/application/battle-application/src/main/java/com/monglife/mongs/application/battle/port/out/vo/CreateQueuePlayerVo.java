package com.monglife.mongs.application.battle.port.out.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateQueuePlayerVo {

    private final Long mongId;

    private final String deviceId;

    private final Long accountId;

    @Builder
    public CreateQueuePlayerVo(Long mongId, String deviceId, Long accountId) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
    }
}
