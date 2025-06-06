package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.mong.model.Mong;

import java.util.Optional;

public interface MongReadPort {

    /**
     * 몽 조회
     * @param mongId 몽 ID
     * @return 몽 도메인 객체
     */
    Optional<Mong> getMongPort(Long mongId);
}
