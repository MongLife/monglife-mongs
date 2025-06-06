package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.battle.model.Match;

import java.util.Optional;

public interface MatchReadPort {

    /**
     * 매치 조회
     * @param matchId 매치 ID
     * @return 매치 도메인 객체
     */
    Optional<Match> getMatchPort(Long matchId);
}
