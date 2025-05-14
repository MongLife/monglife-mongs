package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.model.Match;

public interface MatchPublishPort {

    /**
     * 매치 비동기 응답
     * @param match 매치 도메인 객체
     */
    void publishMatch(Match match);

    /**
     * 매치 강제 중단 비동기 응답
     * @param match 매치 도메인 객체
     */
    void publishStopMatch(Match match);
}
