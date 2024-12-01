package com.monglife.mongs.domain.battle.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FightBattleVo {

    private Integer round;

    private Set<BattlePlayerVo> battlePlayers;

    private Boolean isLastRound;

    @Builder
    public FightBattleVo(Integer round, Set<BattlePlayerVo> battlePlayers, Boolean isLastRound) {
        this.round = round;
        this.battlePlayers = battlePlayers;
        this.isLastRound = isLastRound;
    }
}
