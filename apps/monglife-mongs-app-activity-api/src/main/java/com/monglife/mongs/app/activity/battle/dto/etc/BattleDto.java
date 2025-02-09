package com.monglife.mongs.app.activity.battle.dto.etc;

import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.domain.match.vo.MatchVo;
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

    public static BattleDto of(MatchVo matchVo) {
        return BattleDto.builder()
                .roomId(matchVo.getRoomId())
                .round(matchVo.getRound())
                .isLastRound(matchVo.getIsLastRound())
                .battlePlayers(matchVo.getMatchPlayers())
                .build();
    }
}
