package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.model.QueuePlayer;

public interface QueuePublishPort {

    /**
     * 매치 대기열 등록 실패 비동기 응답
     * @param queuePlayer 매치 대기열 도메인 객체
     */
    void publishCreateQueuePlayerFail(QueuePlayer queuePlayer);
}
