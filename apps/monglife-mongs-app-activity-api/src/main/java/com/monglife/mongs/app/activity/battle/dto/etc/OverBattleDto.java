package com.monglife.mongs.app.activity.battle.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverBattleDto {

    private Long roomId;

    private String winPlayerId;

    private String winMongTypeCode;


    @Builder
    public OverBattleDto(Long roomId, String winPlayerId, String winMongTypeCode) {
        this.roomId = roomId;
        this.winPlayerId = winPlayerId;
        this.winMongTypeCode = winMongTypeCode;
    }
}
