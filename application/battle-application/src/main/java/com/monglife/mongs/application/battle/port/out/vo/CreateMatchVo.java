package com.monglife.mongs.application.battle.port.out.vo;

import com.monglife.mongs.domain.model.MatchPlayer;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class CreateMatchVo {

    private final Integer round;

    private final Integer maxRound;

    private final List<MatchPlayer> matchPlayers;

    @Builder
    public CreateMatchVo(Integer round, Integer maxRound, List<MatchPlayer> matchPlayers) {
        this.round = round;
        this.maxRound = maxRound;
        this.matchPlayers = matchPlayers == null ? Collections.emptyList() : matchPlayers;
    }
}
