package com.monglife.mongs.adapter.out.battle.publish.service;

import com.monglife.mongs.adapter.out.battle.publish.client.BattlePublishClient;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerFailPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.vo.QueuePlayerVo;
import com.monglife.mongs.application.battle.port.out.QueuePublishPort;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.QueuePlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueuePublishService implements QueuePublishPort {

    private final BattlePublishClient battlePublishClient;

    /**
     * 매칭 성공 비동기 응답
     * @param match 매치 도메인 객체
     */
    @Override
    public void publishMatchingQueuePlayerPort(Match match) {

        MatchingQueuePlayerPublishDto matchingQueuePlayerPublishDto = MatchingQueuePlayerPublishDto.builder()
                .matchId(match.getMatchId())
                .matchPlayers(match.getMatchPlayers().stream()
                        .map(matchPlayer -> QueuePlayerVo.builder()
                                .deviceId(matchPlayer.getDeviceId())
                                .playerId(matchPlayer.getPlayerId())
                                .mongId(matchPlayer.getMongId())
                                .mongCode(matchPlayer.getMongCode())
                                .mongName(matchPlayer.getMongName())
                                .build())
                        .toList())
                .build();

        battlePublishClient.publishMatchingQueuePlayer(matchingQueuePlayerPublishDto);
    }

    /**
     * 매치 대기열 등록 실패 비동기 응답
     * @param queuePlayer 매치 대기열 도메인 객체
     */
    @Override
    public void publishMatchingQueuePlayerFailPort(QueuePlayer queuePlayer) {

        MatchingQueuePlayerFailPublishDto matchingQueuePlayerFailPublishDto = MatchingQueuePlayerFailPublishDto.builder()
                .deviceId(queuePlayer.getDeviceId())
                .build();

        battlePublishClient.publishMatchingQueuePlayerFail(matchingQueuePlayerFailPublishDto);
    }
}
