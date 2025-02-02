package com.monglife.mongs.app.activity.battle.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BattleDto {

    private Long roomId;

    private Integer round;

    private Boolean isLastRound;

    private Set<MatchPlayerVo> battlePlayers;

    @Builder
    public BattleDto(Long roomId, Integer round, Boolean isLastRound, Set<MatchPlayerVo> battlePlayers) {
        this.roomId = roomId;
        this.round = round;
        this.isLastRound = isLastRound;
        this.battlePlayers = battlePlayers;
    }
}
