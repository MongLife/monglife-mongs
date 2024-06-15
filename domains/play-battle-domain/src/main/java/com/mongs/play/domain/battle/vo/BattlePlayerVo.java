package com.mongs.play.domain.battle.vo;

import com.mongs.play.domain.battle.code.PickCode;
import com.mongs.play.domain.battle.entity.BattlePlayer;
import lombok.Builder;

@Builder
public record BattlePlayerVo(
        String playerId,
        Long mongId,
        String mongCode,
        Double hp,
        Double attackValue,
        Boolean isBot
) {
    public static BattlePlayerVo of(BattlePlayer battlePlayer) {
        return BattlePlayerVo.builder()
                .playerId(battlePlayer.getPlayerId())
                .mongId(battlePlayer.getMongId())
                .mongCode(battlePlayer.getMongCode())
                .hp(battlePlayer.getHp())
                .attackValue(battlePlayer.getAttackValue())
                .isBot(battlePlayer.getIsBot())
                .build();
    }
}
