package com.monglife.mongs.app.activity.battle.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleDto {

    private String playerId;

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private Boolean isBot;

    @Builder
    public CreateBattleDto(String playerId, Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.playerId = playerId;
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }
}
