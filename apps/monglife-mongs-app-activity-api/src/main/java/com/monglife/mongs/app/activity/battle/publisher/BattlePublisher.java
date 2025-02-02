package com.monglife.mongs.app.activity.battle.publisher;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.GetBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BattlePublisher {

    private final BattleService battleService;

    /**
     * 배틀룸 생성
     * @param createBattleResponseDto 매칭된 플레이어 정보
     * @return 배틀룸 생성 시, 비동기 응답
     */
    @MqttPublish("/battle/search/{topic}")
    public MqttResponseEntity<ResponseDto<CreateBattleResponseDto>> createBattlePublish(List<String> deviceIds, CreateBattleResponseDto createBattleResponseDto) {

        return MqttResponseEntity
                .body(BattleResponse.APP_ACTIVITY_BATTLE_FIND_MATCHING.toResponseDto(createBattleResponseDto))
                .topics(deviceIds);
    }

    /**
     * 배틀룸 입장
     * @param roomId 배틀룸 ID
     * @param getBattleResponseDto 배틀룸 입장 완료 응답 Dto
     * @return 배틀룸 입장 시, 비동기 응답
     */
    @MqttPublish("/battle/match/{topic}")
    public MqttResponseEntity<ResponseDto<GetBattleResponseDto>> enterBattlePublish(Long roomId, GetBattleResponseDto getBattleResponseDto) {

        return MqttResponseEntity
                .body(BattleResponse.APP_ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER.toResponseDto(getBattleResponseDto))
                .topic(roomId.toString());
    }

    /**
     * 배틀 종료
     * @param roomId 배틀룸 ID
     * @param overBattleResponseDto 배틀 종료 응답 Dto
     * @return 모든 플레이어 퇴장 시, 비동기 응답
     */
    @MqttPublish("/battle/match/{topic}")
    public MqttResponseEntity<ResponseDto<OverBattleResponseDto>> overBattlePublish(Long roomId, OverBattleResponseDto overBattleResponseDto) {

        return MqttResponseEntity
                .body(BattleResponse.APP_ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(overBattleResponseDto))
                .topic(roomId.toString());
    }

    /**
     * 배틀 라운드 선택
     * @param roomId 배틀룸 ID
     * @param getBattleResponseDto 배틀 선택 완료 응답 Dto
     * @return 모든 플레이어 라운드 선택 완료 시, 비동기 응답
     */
    @MqttPublish("/battle/match/{topic}")
    public MqttResponseEntity<ResponseDto<GetBattleResponseDto>> pickBattlePublish(Long roomId, GetBattleResponseDto getBattleResponseDto) {

        return MqttResponseEntity
                .body(BattleResponse.APP_ACTIVITY_BATTLE_FIGHT_BATTLE.toResponseDto(getBattleResponseDto))
                .topic(roomId.toString());
    }
}
