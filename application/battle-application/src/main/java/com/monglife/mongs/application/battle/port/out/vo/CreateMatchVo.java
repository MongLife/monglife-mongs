package com.monglife.mongs.application.battle.port.out.vo;

import com.monglife.mongs.domain.enums.MatchStateCode;
import com.monglife.mongs.domain.model.Match;
import com.monglife.mongs.domain.model.MatchPlayer;
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
        this.maxRound = Match.getMaxRound();
        this.matchPlayers = matchPlayers == null ? new ArrayList<>() : matchPlayers;
        this.round = Match.getInitRound();
        this.matchStateCode = Match.getInitMatchStateCode();
    }
}
