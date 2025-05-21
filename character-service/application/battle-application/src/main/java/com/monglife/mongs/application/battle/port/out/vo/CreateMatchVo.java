package com.monglife.mongs.application.battle.port.out.vo;

import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateMatchVo {

    private final Integer maxRound;

    private final List<MatchPlayer> matchPlayers;

    private final Integer round;

    private final MatchStateCode matchStateCode;

    @Builder
    public CreateMatchVo(List<MatchPlayer> matchPlayers) {
        this.maxRound = Match.getInitMaxRound();
        this.matchPlayers = matchPlayers == null ? new ArrayList<>() : matchPlayers;
        this.round = Match.getInitRound();
        this.matchStateCode = Match.getInitMatchStateCode();
    }
}
