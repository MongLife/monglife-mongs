package com.monglife.mongs.app.activity.battle.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OverBattleResponseDto {

    private Long roomId;

    private String winPlayerId;

    private String winMongCode;

    @Builder
    public OverBattleResponseDto(Long roomId, String winPlayerId, String winMongCode) {
        this.roomId = roomId;
        this.winPlayerId = winPlayerId;
        this.winMongCode = winMongCode;
    }
}
