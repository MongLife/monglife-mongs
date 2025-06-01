package com.monglife.mongs.adapter.out.battle.publish.dto.response;

import com.monglife.mongs.adapter.out.battle.publish.vo.MatchPlayerVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchPublishDto {

    private Long matchId;

    private Integer round;

    private Boolean isLastRound;

    private List<MatchPlayerVo> matchPlayers;

    @Builder
    public MatchPublishDto(Long matchId, Integer round, Boolean isLastRound, List<MatchPlayerVo> matchPlayers) {
        this.matchId = matchId;
        this.round = round;
        this.isLastRound = isLastRound;
        this.matchPlayers = matchPlayers;
    }
}
