package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.model.MatchMong;

import java.util.Optional;

public interface MatchMongPersistencePort {

    /**
     * 매치 몽 조회
     * @param mongId 몽 ID
     * @return 매치 몽 도메인 객체
     */
    Optional<MatchMong> getMatchMongPort(Long mongId);

    /**
     * 매치 몽 동기화
     * @param matchMong 매치 몽 도메인 객체
     * @return 매치 몽 도메인 객체
     */
    Optional<MatchMong> saveMatchMongPort(MatchMong matchMong);
}
