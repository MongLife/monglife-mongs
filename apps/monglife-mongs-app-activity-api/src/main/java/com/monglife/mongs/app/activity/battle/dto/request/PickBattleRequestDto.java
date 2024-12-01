package com.monglife.mongs.app.activity.battle.dto.request;

import com.monglife.mongs.domain.battle.enums.BattleRoundCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PickBattleRequestDto {

    private String playerId;

    private String targetPlayerId;

    private BattleRoundCode pickCode;

    @Builder
    public PickBattleRequestDto(String playerId, String targetPlayerId, BattleRoundCode pickCode) {
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.pickCode = pickCode;
    }
}
