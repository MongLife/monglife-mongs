package com.monglife.mongs.adapter.out.battle.publish.service;

import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.battle.model.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BattlePublishService implements MatchPublishPort {

    /**
     * 매치 비동기 응답
     * @param match 매치 도메인 객체
     */
    @Override
    public void publishMatch(Match match) {

    }

    /**
     * 매치 강제 중단 비동기 응답
     * @param match 매치 도메인 객체
     */
    @Override
    public void publishStopMatch(Match match) {

    }
}
