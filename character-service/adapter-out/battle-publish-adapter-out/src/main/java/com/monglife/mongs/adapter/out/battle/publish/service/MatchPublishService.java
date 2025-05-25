package com.monglife.mongs.adapter.out.battle.publish.service;

import com.monglife.mongs.adapter.out.battle.publish.client.BattlePublishClient;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchEndPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.vo.MatchPlayerVo;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchPublishService implements MatchPublishPort {

    private final BattlePublishClient battlePublishClient;

    /**
     * 매치 비동기 응답
     * @param match 매치 도메인 객체
     */
    @Override
    public void publishMatchPort(Match match) {
        MatchPublishDto matchPublishDto = MatchPublishDto.builder()
                .matchId(match.getMatchId())
                .round(match.getRound())
                .isLastRound(match.isEnd())
                .matchPlayers(match.getMatchPlayers().stream()
                        .map(matchPlayer -> MatchPlayerVo.builder()
                                .playerId(matchPlayer.getPlayerId())
                                .deviceId(matchPlayer.getDeviceId())
                                .mongTypeCode(matchPlayer.getMongTypeCode())
                                .mongTypeName(matchPlayer.getMongTypeName())
                                .hp(matchPlayer.getHp())
                                .roundCode(matchPlayer.getMatchRoundCode())
                                .build())
                        .toList())
                .build();
        if (match.getRound() == 0) {
            battlePublishClient.publishMatchPlayersEntered(matchPublishDto);
        } else {
            battlePublishClient.publishMatch(matchPublishDto);
        }
    }

    /**
     * 매치 강제 중단 비동기 응답
     * @param match 매치 도메인 객체
     * @param matchPlayer 승리한 매치 플레이어 도메인 객체
     */
    @Override
    public void publishMatchEndPort(Match match, MatchPlayer matchPlayer) {

        MatchEndPublishDto matchEndPublishDto = MatchEndPublishDto.builder()
                .matchId(match.getMatchId())
                .playerId(matchPlayer.getPlayerId())
                .mongName(matchPlayer.getMongName())
                .mongTypeCode(matchPlayer.getMongTypeCode())
                .mongTypeName(matchPlayer.getMongTypeName())
                .build();

        battlePublishClient.publishMatchEnd(matchEndPublishDto);
    }
}
