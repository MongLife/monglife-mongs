package com.monglife.mongs.app.battle.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBattleDto {

    private Long mongId;

    private String deviceId;

    private Long accountId;

    private Boolean isBot;

    @Builder
    public CreateBattleDto(Long mongId, String deviceId, Long accountId, Boolean isBot) {
        this.mongId = mongId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.isBot = isBot;
    }
}
