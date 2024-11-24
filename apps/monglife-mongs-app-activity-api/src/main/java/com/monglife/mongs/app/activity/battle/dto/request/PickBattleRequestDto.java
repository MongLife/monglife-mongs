package com.monglife.mongs.app.activity.battle.dto.request;

import com.monglife.mongs.app.activity.battle.enums.BattleRoundCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickBattleRequestDto {

    private Long roomId;

    private String playerId;

    private String targetPlayerId;

    private BattleRoundCode pickCode;

    @Builder
    public PickBattleRequestDto(Long roomId, String playerId, String targetPlayerId, BattleRoundCode pickCode) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.pickCode = pickCode;
    }
}
