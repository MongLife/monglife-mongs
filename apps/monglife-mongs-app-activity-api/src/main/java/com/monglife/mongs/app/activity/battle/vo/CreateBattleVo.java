package com.monglife.mongs.app.activity.battle.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateBattleVo {

    private final String deviceId;

    private final Long accountId;

    private final Long mongId;

    private final Boolean isBot;

    @Builder
    public CreateBattleVo(String deviceId, Long accountId, Long mongId, Boolean isBot) {
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.isBot = isBot;
    }
}
