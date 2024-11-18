package com.monglife.mongs.app.battle.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnterBattleDto {

    private String roomId;

    private String playerId;

    @Builder
    public EnterBattleDto(String roomId, String playerId) {
        this.roomId = roomId;
        this.playerId = playerId;
    }
}
