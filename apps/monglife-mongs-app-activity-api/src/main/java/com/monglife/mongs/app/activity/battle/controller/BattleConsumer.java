package com.monglife.mongs.app.activity.battle.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.etc.CreateBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.dto.request.CreateBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.EnterBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.ExitBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.PickBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.FightBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.app.activity.battle.vo.CreateBattleVo;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import com.monglife.mongs.module.mqtt.annotation.MqttConsumer;
import com.monglife.mongs.module.mqtt.annotation.MqttMapping;
import com.monglife.mongs.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@MqttConsumer
@MqttMapping("/battle/match")
@RequiredArgsConstructor
public class BattleConsumer {

    private final BattleService battleService;

    /**
     * 배틀룸 생성
     * @param createBattleRequestDto 매칭된 플레이어 정보
     * @return 배틀룸 생성 시, 비동기 응답
     */
    @MqttPublish
    public MqttResponseEntity<ResponseDto<CreateBattleResponseDto>> createBattle(CreateBattleRequestDto createBattleRequestDto) {

        Set<CreateBattleVo> createBattleVoSet = createBattleRequestDto.getCreateBattleVoSet();

        CreateBattleDto createBattleDto = battleService.createBattle(createBattleVoSet);

        List<String> topics = createBattleRequestDto.getCreateBattleVoSet().stream()
                .filter(createBattleVo -> !createBattleVo.getIsBot())
                .map(createBattleVo -> "search/" + createBattleVo.getDeviceId())
                .toList();

        Long roomId = createBattleDto.getRoomId();
        Set<MatchPlayerVo> battlePlayers = createBattleDto.getBattlePlayers();

        return MqttResponseEntity
                .body(BattleResponse.APP_ACTIVITY_BATTLE_FIND_MATCHING.toResponseDto(CreateBattleResponseDto.builder()
                        .roomId(roomId)
                        .battlePlayers(battlePlayers)
                        .build()))
                .topics(topics);
    }

    /**
     * 배틀룸 입장
     * @param roomId 배틀룸 ID
     * @param enterBattleRequestDto player ID
     * @return 입장 완료 시, 비동기 응답
     */
    @MqttPublish
    @MqttMapping("/{roomId}/enter")
    public MqttResponseEntity<ResponseDto<FightBattleResponseDto>> enterBattle(
            @PathVariable("roomId") Long roomId,
            @MqttPayload EnterBattleRequestDto enterBattleRequestDto
    ) {

        String playerId = enterBattleRequestDto.getPlayerId();

        FightBattleDto fightBattleDto = battleService.enterBattle(roomId, playerId);

        if (fightBattleDto != null) {

            List<String> topics = List.of("match/" + roomId);

            return MqttResponseEntity
                    .body(BattleResponse.APP_ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER.toResponseDto(FightBattleResponseDto.builder()
                            .roomId(fightBattleDto.getRoomId())
                            .round(fightBattleDto.getRound())
                            .isLastRound(fightBattleDto.getIsLastRound())
                            .battlePlayers(fightBattleDto.getBattlePlayers())
                            .build()))
                    .topics(topics);

        } else {
            return MqttResponseEntity.body();
        }
    }

    /**
     * 배틀룸 퇴장
     * @param roomId 배틀룸 ID
     * @param exitBattleRequestDto player ID
     * @return 모든 플레이어 퇴장 시, 비동기 응답
     */
    @MqttPublish
    @MqttMapping("/{roomId}/exit")
    public MqttResponseEntity<ResponseDto<OverBattleResponseDto>> exitBattle(
            @PathVariable("roomId") Long roomId,
            @MqttPayload ExitBattleRequestDto exitBattleRequestDto
    ) {

        String playerId = exitBattleRequestDto.getPlayerId();

        OverBattleDto overBattleDto = battleService.exitBattle(roomId, playerId);

        if (overBattleDto != null) {

            List<String> topics = List.of("match/" + roomId);

            return MqttResponseEntity
                    .body(BattleResponse.APP_ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(OverBattleResponseDto.builder()
                            .roomId(overBattleDto.getRoomId())
                            .winPlayerId(overBattleDto.getWinPlayerId())
                            .winMongTypeCode(overBattleDto.getWinMongTypeCode())
                            .build()))
                    .topics(topics);
        } else {
            return MqttResponseEntity.body();
        }
    }

    /**
     * 배틀 라운드 선택
     * @param roomId 배틀룸 ID
     * @param pickBattleRequestDto 선택 정보
     * @return 모든 플레이어 라운드 선택 완료 시, 비동기 응답
     */
    @MqttPublish
    @MqttMapping("/{roomId}/pick")
    public MqttResponseEntity<ResponseDto<FightBattleResponseDto>> pickBattle(
            @PathVariable("roomId") Long roomId,
            @MqttPayload PickBattleRequestDto pickBattleRequestDto
    ) {

        String playerId = pickBattleRequestDto.getPlayerId();
        String targetPlayerId = pickBattleRequestDto.getTargetPlayerId();
        MatchRoundCode matchRoundCode = pickBattleRequestDto.getPickCode();

        FightBattleDto fightBattleDto = battleService.pickBattle(roomId, playerId, targetPlayerId, matchRoundCode);

        if (fightBattleDto != null) {

            List<String> topics = List.of("match/" + roomId);

            return MqttResponseEntity
                    .body(BattleResponse.APP_ACTIVITY_BATTLE_FIGHT_BATTLE.toResponseDto(FightBattleResponseDto.builder()
                            .roomId(roomId)
                            .round(fightBattleDto.getRound())
                            .battlePlayers(fightBattleDto.getBattlePlayers())
                            .isLastRound(fightBattleDto.getIsLastRound())
                            .build()))
                    .topics(topics);
        } else {
            return MqttResponseEntity.body();
        }
    }
}
