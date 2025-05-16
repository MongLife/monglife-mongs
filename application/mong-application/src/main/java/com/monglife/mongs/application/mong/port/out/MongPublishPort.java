package com.monglife.mongs.application.mong.port.out;

import com.monglife.mongs.domain.mong.model.Mong;

public interface MongPublishPort {

    /**
     * 몽 정보 비동기 응답
     * @param mong 몽 도메인 객체
     */
    void publishMongPort(Mong mong);
}
