package com.monglife.mongs.app.battle.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExitBattleDto {

    private String roomId;

    private String playerId;

    @Builder
    public ExitBattleDto(String roomId, String playerId) {
        this.roomId = roomId;
        this.playerId = playerId;
    }
}
