package com.monglife.mongs.app.battle.dto.etc;

import com.monglife.mongs.app.battle.vo.BattlePlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FightBattleDto {

    private Integer round;

    private Set<BattlePlayerVo> battlePlayers;

    private Boolean isLastRound;

    @Builder
    public FightBattleDto(Integer round, Set<BattlePlayerVo> battlePlayers, Boolean isLastRound) {
        this.round = round;
        this.battlePlayers = battlePlayers;
        this.isLastRound = isLastRound;
    }
}
