package com.mongs.play.domain.battle.vo;

import com.mongs.play.domain.battle.entity.BattlePlayer;
import lombok.Builder;

@Builder
public record BattlePlayerVo(
        String playerId,
        Long mongId,
        String mongCode,
        Double hp,
        Double attackValue,
        Double healValue,
        String pick
) {
    public static BattlePlayerVo of(BattlePlayer battlePlayer) {
        return BattlePlayerVo.builder()
                .playerId(battlePlayer.getPlayerId())
                .mongId(battlePlayer.getMongId())
                .mongCode(battlePlayer.getMongCode())
                .hp(battlePlayer.getHp())
                .attackValue(battlePlayer.getAttackValue())
                .healValue(battlePlayer.getHealValue())
                .pick(battlePlayer.getPick().name())
                .build();
    }
}
