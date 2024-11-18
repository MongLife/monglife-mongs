package com.monglife.mongs.app.battle.dto.etc;

import com.monglife.mongs.app.battle.global.enums.BattleRoundCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickBattleDto {

    private String roomId;

    private String playerId;

    private String targetPlayerId;

    private BattleRoundCode pickCode;

    @Builder
    public PickBattleDto(String roomId, String playerId, String targetPlayerId, BattleRoundCode pickCode) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.pickCode = pickCode;
    }
}
