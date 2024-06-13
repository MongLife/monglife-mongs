package com.mongs.play.domain.battle.vo;

import lombok.Builder;

@Builder
public record BattlePlayerVo(
        Long mongId,
        String mongCode,
        Double attackValue,
        Double healValue,
        Boolean isBot
) {
}
