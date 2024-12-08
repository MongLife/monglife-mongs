package com.monglife.mongs.app.activity.battle.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.request.EnterBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.ExitBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.PickBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.response.FightBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import com.monglife.mongs.domain.battle.dto.etc.EnterBattleDto;
import com.monglife.mongs.domain.battle.dto.etc.ExitBattleDto;
import com.monglife.mongs.domain.battle.dto.etc.PickBattleDto;
import com.monglife.mongs.domain.battle.enums.BattleRoundCode;
import com.monglife.mongs.domain.battle.service.BattleService;
import com.monglife.mongs.domain.battle.vo.FightBattleVo;
import com.monglife.mongs.domain.battle.vo.OverBattleVo;
import com.monglife.mongs.module.mqtt.annotation.MqttConsumer;
import com.monglife.mongs.module.mqtt.annotation.MqttMapping;
import com.monglife.mongs.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.module.mqtt.annotation.MqttPublish;
import com.monglife.mongs.module.mqtt.dto.MqttResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@MqttConsumer @MqttMapping("/match")
@RestController @RequestMapping("/activity/battle/match")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;


    @MqttPublish
    @MqttMapping("/{roomId}/enter")
    public MqttResponseEntity<ResponseDto<FightBattleResponseDto>> enterBattle(
            @PathVariable("roomId") Long roomId,
            @MqttPayload EnterBattleRequestDto enterBattleRequestDto
    ) {

        String playerId = enterBattleRequestDto.getPlayerId();

        EnterBattleDto enterBattleDto = battleService.enterBattle(roomId, playerId);

        Boolean isEnterAll = enterBattleDto.getIsEnterAll();
        FightBattleVo fightBattleVo = enterBattleDto.getFightBattleVo();

        if (isEnterAll && fightBattleVo != null) {
            List<String> topics = List.of("match/" + roomId);

            FightBattleResponseDto fightBattleResponseDto = FightBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(fightBattleVo.getRound())
                    .battlePlayers(fightBattleVo.getBattlePlayers())
                    .isLastRound(fightBattleVo.getIsLastRound())
                    .build();

            return MqttResponseEntity
                    .body(BattleResponse.ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER.toResponseDto(fightBattleResponseDto))
                    .topics(topics);
        }

        return MqttResponseEntity.body();
    }

    @MqttPublish
    @MqttMapping("/{roomId}/exit")
    public MqttResponseEntity<ResponseDto<OverBattleResponseDto>> exitBattle(
            @PathVariable("roomId") Long roomId,
            @MqttPayload ExitBattleRequestDto exitBattleRequestDto
    ) {

        String playerId = exitBattleRequestDto.getPlayerId();

        ExitBattleDto exitBattleDto = battleService.exitBattle(roomId, playerId);

        Boolean isExitAll = exitBattleDto.getIsExitAll();
        List<OverBattleVo> overBattleVos = exitBattleDto.getOverBattleVos();

        if (isExitAll && overBattleVos != null) {
            // 남은 플레이어 1명 승리로 처리
            battleService.overBattle(roomId);

            List<String> topics = List.of("match/" + roomId);

            String winPlayerId = overBattleVos.isEmpty() ? "" : overBattleVos.get(0).getPlayerId();
            String winMongTypeCode = overBattleVos.isEmpty() ? "" : overBattleVos.get(0).getMongTypeCode();

            OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                    .roomId(roomId)
                    .winPlayerId(winPlayerId)
                    .winMongTypeCode(winMongTypeCode)
                    .build();

            return MqttResponseEntity
                    .body(BattleResponse.ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(overBattleResponseDto))
                    .topics(topics);
        }

        return MqttResponseEntity.body();
    }

    @MqttPublish
    @MqttMapping("/{roomId}/pick")
    public MqttResponseEntity<ResponseDto<FightBattleResponseDto>> pickBattle(
            @PathVariable("roomId") Long roomId,
            @MqttPayload PickBattleRequestDto pickBattleRequestDto
    ) {

        String playerId = pickBattleRequestDto.getPlayerId();
        String targetPlayerId = pickBattleRequestDto.getTargetPlayerId();
        BattleRoundCode battleRoundCode = pickBattleRequestDto.getPickCode();

        PickBattleDto pickBattleDto = battleService.pickBattle(roomId, playerId, targetPlayerId, battleRoundCode);

        Boolean isPickAll = pickBattleDto.getIsPickAll();
        FightBattleVo fightBattleVo = pickBattleDto.getFightBattleVo();

        if (isPickAll && fightBattleVo != null) {
            // 응답 전송 토픽
            List<String> topics = List.of("match/" + roomId);

            // 결과 값 반환
            FightBattleResponseDto fightBattleResponseDto = FightBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(fightBattleVo.getRound())
                    .battlePlayers(fightBattleVo.getBattlePlayers())
                    .isLastRound(fightBattleVo.getIsLastRound())
                    .build();

            // 마지막 라운드 인 경우 배틀 종료 처리
            if (fightBattleVo.getIsLastRound()) {
                battleService.overBattle(roomId);
            }

            return MqttResponseEntity
                    .body(BattleResponse.ACTIVITY_BATTLE_FIGHT_BATTLE.toResponseDto(fightBattleResponseDto))
                    .topics(topics);
        }

        return MqttResponseEntity.body();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<OverBattleResponseDto>> overBattle(@PathVariable("roomId") Long roomId) {

        List<OverBattleVo> overBattleVos = battleService.findOverBattle(roomId);

        String winPlayerId = overBattleVos.isEmpty() ? "" : overBattleVos.get(0).getPlayerId();
        String winMongTypeCode = overBattleVos.isEmpty() ? "" : overBattleVos.get(0).getMongTypeCode();

        OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                .roomId(roomId)
                .winPlayerId(winPlayerId)
                .winMongTypeCode(winMongTypeCode)
                .build();

        return ResponseEntity.ok(BattleResponse.ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(overBattleResponseDto));
    }
}
