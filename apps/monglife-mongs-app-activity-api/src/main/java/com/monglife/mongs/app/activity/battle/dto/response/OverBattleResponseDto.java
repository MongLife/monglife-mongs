package com.monglife.mongs.app.activity.battle.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverBattleResponseDto {

    private Long roomId;

    private String winPlayerId;

    private String winMongTypeCode;

    @Builder
    public OverBattleResponseDto(Long roomId, String winPlayerId, String winMongTypeCode) {
        this.roomId = roomId;
        this.winPlayerId = winPlayerId;
        this.winMongTypeCode = winMongTypeCode;
    }
}
