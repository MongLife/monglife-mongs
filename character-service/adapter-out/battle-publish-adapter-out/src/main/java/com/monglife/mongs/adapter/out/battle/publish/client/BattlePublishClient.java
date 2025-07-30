package com.monglife.mongs.adapter.out.battle.publish.client;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.mqtt.annotation.MqttPublish;
import com.monglife.module.mqtt.dto.MqttResponseEntity;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerFailPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.dto.response.MatchingQueuePlayerPublishDto;
import com.monglife.mongs.adapter.out.battle.publish.enums.AdapterOutPublishBattleResponse;
import com.monglife.mongs.adapter.out.battle.publish.vo.QueuePlayerVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattlePublishClient {

    /**
     * 매치 생성 비동기 응답
     * @param matchingQueuePlayerPublishDto 매칭된 매치 플레이어 정보
     * @return 매치 생성 시, 비동기 응답
     */
    @MqttPublish("/battle/queue/{topic}")
    public MqttResponseEntity<ResponseDto<MatchingQueuePlayerPublishDto>> publishMatchingQueuePlayer(MatchingQueuePlayerPublishDto matchingQueuePlayerPublishDto) {

        List<String> topics = matchingQueuePlayerPublishDto.getMatchPlayers().stream()
                .map(QueuePlayerVo::getDeviceId)
                .toList();

        return MqttResponseEntity
                .body(AdapterOutPublishBattleResponse.BATTLE_PUBLISH_MATCHING_QUEUE_PLAYER.toResponseDto(matchingQueuePlayerPublishDto))
                .topics(topics);
    }

    /**
     * 매치 대기열 재등록 실패 비동기 응답
     * @param matchingQueuePlayerFailPublishDto 대기열 재등록 시도한 매치 플레이어 정보
     * @return 대기열 등록 실패 시, 비동기 응답
     */
    @MqttPublish("/battle/queue/{topic}")
    public MqttResponseEntity<ResponseDto<MatchingQueuePlayerFailPublishDto>> publishMatchingQueuePlayerFail(MatchingQueuePlayerFailPublishDto matchingQueuePlayerFailPublishDto) {

        String topic = matchingQueuePlayerFailPublishDto.getDeviceId();

        return MqttResponseEntity
                .body(AdapterOutPublishBattleResponse.BATTLE_PUBLISH_MATCHING_QUEUE_PLAYER_FAIL.toResponseDto(matchingQueuePlayerFailPublishDto))
                .topic(topic);
    }

    /**
     * 모든 매치 플레이어 입장 완료 비동기 응답
     * @param matchPublishDto 매치 정보
     * @return 모든 매치 플레이어 입장 시, 비동기 응답
     */
    @MqttPublish("/battle/match/{topic}")
    public MqttResponseEntity<ResponseDto<MatchPublishDto>> publishMatchPlayersEntered(MatchPublishDto matchPublishDto) {

        String topic = String.valueOf(matchPublishDto.getMatchId());

        return MqttResponseEntity
                .body(AdapterOutPublishBattleResponse.BATTLE_PUBLISH_MATCH_PLAYERS_ENTERED.toResponseDto(matchPublishDto))
                .topic(topic);
    }

    /**
     * 매치 라운드 정보 비동기 응답
     * @param matchPublishDto 매치 정보
     * @return 모든 매치 플레이어 라운드 선택 완료 시, 비동기 응답
     */
    @MqttPublish("/battle/match/{topic}")
    public MqttResponseEntity<ResponseDto<MatchPublishDto>> publishMatch(MatchPublishDto matchPublishDto) {

        String topic = String.valueOf(matchPublishDto.getMatchId());

        return MqttResponseEntity
                .body(AdapterOutPublishBattleResponse.BATTLE_PUBLISH_MATCH.toResponseDto(matchPublishDto))
                .topic(topic);
    }

    /**
     * 매치 종료
     * @param matchPublishDto 매치 정보
     * @return 매치 중 종료 시, 비동기 응답
     */
    @MqttPublish("/battle/match/{topic}")
    public MqttResponseEntity<ResponseDto<MatchPublishDto>> publishMatchEnd(MatchPublishDto matchPublishDto) {

        String topic = String.valueOf(matchPublishDto.getMatchId());

        return MqttResponseEntity
                .body(AdapterOutPublishBattleResponse.BATTLE_PUBLISH_MATCH_END.toResponseDto(matchPublishDto))
                .topic(topic);
    }
}
