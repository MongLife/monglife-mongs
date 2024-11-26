package com.monglife.mongs.app.activity.battle.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.dto.request.EnterBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.ExitBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.PickBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.response.BattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.FightBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.enums.BattleRoundCode;
import com.monglife.mongs.app.activity.battle.enums.BattleStateCode;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/activity/battle")
public class BattleController {

    private final BattleService battleService;


    @PostMapping("/enter")
    public ResponseDto<BattleResponseDto<FightBattleResponseDto>> enterBattle(@RequestBody EnterBattleRequestDto enterBattleRequestDto) {

        Long roomId = enterBattleRequestDto.getRoomId();
        String playerId = enterBattleRequestDto.getPlayerId();

        Pair<Boolean, FightBattleDto> enterBattlePair = battleService.enterBattle(roomId, playerId);

        Boolean isEnterAll = enterBattlePair.a;
        FightBattleDto fightBattleDto = enterBattlePair.b;

        if (isEnterAll && fightBattleDto != null) {
            List<String> topics = List.of(String.valueOf(roomId));

            FightBattleResponseDto fightBattleResponseDto = FightBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(fightBattleDto.getRound())
                    .battlePlayers(fightBattleDto.getBattlePlayers())
                    .isLastRound(fightBattleDto.getIsLastRound())
                    .build();

            // 게임 시작 시그널 반환
            BattleResponseDto<FightBattleResponseDto> battleResponseDto = BattleResponseDto.<FightBattleResponseDto>builder()
                    .code(BattleStateCode.BATTLE_FIGHT)
                    .topics(topics)
                    .data(fightBattleResponseDto)
                    .build();

            return ActivityResponse.ACTIVITY_BATTLE_ENTER_ALL_BATTLE_PLAYER.toResponseDto(battleResponseDto);
        }

        return ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_BATTLE_RESPONSE.toResponseDto(null);
    }

    @DeleteMapping("/exit")
    public ResponseDto<BattleResponseDto<OverBattleResponseDto>> exitBattle(@RequestBody ExitBattleRequestDto exitBattleRequestDto) {

        Long roomId = exitBattleRequestDto.getRoomId();
        String playerId = exitBattleRequestDto.getPlayerId();

        Pair<Boolean, List<OverBattleDto>> exitBattlePair = battleService.exitBattle(roomId, playerId);

        Boolean isExitAll = exitBattlePair.a;
        List<OverBattleDto> overBattleDtos = exitBattlePair.b;

        if (isExitAll && overBattleDtos != null) {
            // 남은 플레이어 1명 승리로 처리
            battleService.overBattle(roomId);

            List<String> topics = List.of(String.valueOf(roomId));

            String winPlayerId = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getPlayerId();
            String winMongTypeCode = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getMongTypeCode();

            OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                    .roomId(roomId)
                    .winPlayerId(winPlayerId)
                    .winMongTypeCode(winMongTypeCode)
                    .build();

            // 게임 끝 시그널 반환
            BattleResponseDto<OverBattleResponseDto> battleResponseDto = BattleResponseDto.<OverBattleResponseDto>builder()
                    .code(BattleStateCode.BATTLE_OVER)
                    .topics(topics)
                    .data(overBattleResponseDto)
                    .build();

            return ActivityResponse.ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(battleResponseDto);
        }

        return ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_BATTLE_RESPONSE.toResponseDto(null);
    }

    @PostMapping("/pick")
    public ResponseDto<BattleResponseDto<FightBattleResponseDto>> pickBattle(@RequestBody PickBattleRequestDto pickBattleRequestDto) {

        Long roomId = pickBattleRequestDto.getRoomId();
        String playerId = pickBattleRequestDto.getPlayerId();
        String targetPlayerId = pickBattleRequestDto.getTargetPlayerId();
        BattleRoundCode battleRoundCode = pickBattleRequestDto.getPickCode();

        Pair<Boolean, FightBattleDto> fightBattleDtoPair = battleService.pickBattle(roomId, playerId, targetPlayerId, battleRoundCode);

        Boolean isPickAll = fightBattleDtoPair.a;
        FightBattleDto fightBattleDto = fightBattleDtoPair.b;

        if (isPickAll && fightBattleDto != null) {
            // 응답 전송 토픽
            List<String> topics = List.of(String.valueOf(roomId));

            // 결과 값 반환
            FightBattleResponseDto fightBattleResponseDto = FightBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(fightBattleDto.getRound())
                    .battlePlayers(fightBattleDto.getBattlePlayers())
                    .isLastRound(fightBattleDto.getIsLastRound())
                    .build();

            // 라운드 결과 정보 반환
            BattleResponseDto<FightBattleResponseDto> battleResponseDto = BattleResponseDto.<FightBattleResponseDto>builder()
                    .code(BattleStateCode.BATTLE_FIGHT)
                    .topics(topics)
                    .data(fightBattleResponseDto)
                    .build();

            // 마지막 라운드 인 경우 배틀 종료 처리
            if (fightBattleDto.getIsLastRound()) {
                battleService.overBattle(roomId);
            }

            return ActivityResponse.ACTIVITY_BATTLE_FIGHT_BATTLE.toResponseDto(battleResponseDto);
        }

        return ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_BATTLE_RESPONSE.toResponseDto(null);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<OverBattleResponseDto>> overBattle(@PathVariable("roomId") Long roomId) {

        List<OverBattleDto> overBattleDtos = battleService.findOverBattle(roomId);

        String winPlayerId = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getPlayerId();
        String winMongTypeCode = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getMongTypeCode();

        OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                .roomId(roomId)
                .winPlayerId(winPlayerId)
                .winMongTypeCode(winMongTypeCode)
                .build();

        return ResponseEntity.ok(ActivityResponse.ACTIVITY_BATTLE_OVER_BATTLE.toResponseDto(overBattleResponseDto));
    }
}
