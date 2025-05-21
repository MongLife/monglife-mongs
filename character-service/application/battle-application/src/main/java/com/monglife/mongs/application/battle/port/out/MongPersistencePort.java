package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.mong.model.Mong;

import java.util.Optional;

public interface MongPersistencePort {

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    Optional<Mong> getMongPort(Long mongId);

    /**
     * 몽 동기화
     * @param mong 몽 도메인 객체
     * @return 몽 도메인 객체
     */
    Optional<Mong> saveMongPort(Mong mong);
}
