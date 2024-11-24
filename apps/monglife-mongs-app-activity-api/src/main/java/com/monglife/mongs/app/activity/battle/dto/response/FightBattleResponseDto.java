package com.monglife.mongs.app.activity.battle.dto.response;

import com.monglife.mongs.app.activity.battle.vo.BattlePlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FightBattleResponseDto {

    private Long roomId;

    private Integer round;

    private Boolean isLastRound;

    private Set<BattlePlayerVo> battlePlayers;

    @Builder
    public FightBattleResponseDto(Long roomId, Integer round, Boolean isLastRound, Set<BattlePlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.round = round;
        this.isLastRound = isLastRound;
        this.battlePlayers = battlePlayers;
    }
}
