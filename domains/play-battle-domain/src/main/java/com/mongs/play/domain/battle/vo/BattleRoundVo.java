package com.mongs.play.domain.battle.vo;

import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.entity.BattleRoom;
import com.mongs.play.domain.battle.entity.BattleRound;
import lombok.Builder;

import java.util.List;

@Builder
public record BattleRoundVo(
        String playerId,
        String targetPlayerId,
        Integer round,
        PickCode pick
) {
    public static BattleRoundVo of(BattleRound battleRound) {
        return BattleRoundVo.builder()
                .playerId(battleRound.getPlayerId())
                .targetPlayerId(battleRound.getTargetPlayerId())
                .round(battleRound.getRound())
                .pick(battleRound.getPick())
                .build();
    }
}
