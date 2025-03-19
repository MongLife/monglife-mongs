package com.monglife.mongs.app.activity.battle.dto.request;

import com.monglife.mongs.domain.match.enums.MatchRoundCode;
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

    private MatchRoundCode pickCode;

    @Builder
    public PickBattleRequestDto(String playerId, String targetPlayerId, MatchRoundCode pickCode) {
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.pickCode = pickCode;
    }
}
