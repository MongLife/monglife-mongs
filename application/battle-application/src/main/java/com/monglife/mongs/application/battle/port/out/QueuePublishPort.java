package com.monglife.mongs.application.battle.port.out;

import com.monglife.mongs.domain.model.Match;
import com.monglife.mongs.domain.model.QueuePlayer;

public interface QueuePublishPort {

    /**
     * 매칭 성공 비동기 응답
     * @param match 매치 도메인 객체
     */
    void publishMatchingQueuePlayer(Match match);

    /**
     * 매치 대기열 등록 실패 비동기 응답
     * @param queuePlayer 매치 대기열 도메인 객체
     */
    void publishMatchingQueuePlayerFail(QueuePlayer queuePlayer);
}
