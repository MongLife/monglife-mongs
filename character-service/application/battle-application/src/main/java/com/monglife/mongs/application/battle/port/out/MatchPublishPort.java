package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;

public interface MatchPublishPort {

    /**
     * 매치 비동기 응답
     * @param match 매치 도메인 객체
     */
    void publishMatchPort(Match match);

    /**
     * 매치 강제 중단 비동기 응답
     * @param match 매치 도메인 객체
     */
    void publishMatchEndPort(Match match, MatchPlayer matchPlayer);
}
