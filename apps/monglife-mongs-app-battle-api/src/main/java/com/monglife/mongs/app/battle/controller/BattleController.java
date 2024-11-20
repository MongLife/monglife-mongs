package com.monglife.mongs.app.battle.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.battle.dto.etc.FightBattleDto;
import com.monglife.mongs.app.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.battle.dto.request.EnterBattleRequestDto;
import com.monglife.mongs.app.battle.dto.request.ExitBattleRequestDto;
import com.monglife.mongs.app.battle.dto.request.PickBattleRequestDto;
import com.monglife.mongs.app.battle.dto.response.FightBattleResponseDto;
import com.monglife.mongs.app.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.battle.global.dto.BattleResponseDto;
import com.monglife.mongs.app.battle.global.enums.BattleResponse;
import com.monglife.mongs.app.battle.global.enums.BattleRoundCode;
import com.monglife.mongs.app.battle.global.enums.BattleStateCode;
import com.monglife.mongs.app.battle.service.BattleService;
import com.monglife.mongs.app.battle.vo.BattlePlayerVo;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/battle")
public class BattleController {

    private final BattleService battleService;


    public ResponseDto<BattleResponseDto<FightBattleResponseDto>> enterBattle(EnterBattleRequestDto enterBattleRequestDto) {

        Long roomId = enterBattleRequestDto.getRoomId();
        String playerId = enterBattleRequestDto.getPlayerId();

        Pair<Boolean, FightBattleDto> enterBattlePair = battleService.enterBattle(roomId, playerId);

        List<String> topics = List.of(String.valueOf(roomId));

        Boolean isEnterAll = enterBattlePair.a;

        if (isEnterAll) {
            FightBattleDto fightBattleDto = enterBattlePair.b;

            FightBattleResponseDto fightBattleResponseDto = FightBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(fightBattleDto.getRound())
                    .battlePlayers(fightBattleDto.getBattlePlayers())
                    .build();

            // 게임 시작 시그널 반환
            BattleResponseDto<FightBattleResponseDto> battleResponseDto = BattleResponseDto.<FightBattleResponseDto>builder()
                    .code(BattleStateCode.BATTLE_FIGHT)
                    .topics(topics)
                    .data(fightBattleResponseDto)
                    .isLastRound(fightBattleDto.getIsLastRound())
                    .build();

            return BattleResponse.BATTLE_ENTER_ALL_BATTLE_PLAYER.toResponseDto(battleResponseDto);
        }

        return BattleResponse.BATTLE_NOT_EXISTS_BATTLE_RESPONSE.toResponseDto(null);
    }

    public ResponseDto<BattleResponseDto<OverBattleResponseDto>> exitBattle(ExitBattleRequestDto exitBattleRequestDto) {

        Long roomId = exitBattleRequestDto.getRoomId();
        String playerId = exitBattleRequestDto.getPlayerId();

        Boolean isExitAll = battleService.exitBattle(roomId, playerId);

        if (isExitAll) {
            // 남은 플레이어 1명 승리로 처리
            List<OverBattleDto> overBattleDtos = battleService.overBattle(roomId);

            List<String> topics = List.of(String.valueOf(roomId));

            String winPlayerId = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getPlayerId();
            String winMongCode = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getMongCode();

            OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                    .roomId(roomId)
                    .winPlayerId(winPlayerId)
                    .winMongCode(winMongCode)
                    .build();

            // 게임 끝 시그널 반환
            BattleResponseDto<OverBattleResponseDto> battleResponseDto = BattleResponseDto.<OverBattleResponseDto>builder()
                    .code(BattleStateCode.BATTLE_OVER)
                    .topics(topics)
                    .data(overBattleResponseDto)
                    .isLastRound(Boolean.TRUE)
                    .build();

            return BattleResponse.BATTLE_OVER_BATTLE.toResponseDto(battleResponseDto);

        }

        return BattleResponse.BATTLE_NOT_EXISTS_BATTLE_RESPONSE.toResponseDto(null);
    }

    public ResponseDto<BattleResponseDto<FightBattleResponseDto>> pickBattle(PickBattleRequestDto pickBattleRequestDto) {

        Long roomId = pickBattleRequestDto.getRoomId();
        String playerId = pickBattleRequestDto.getPlayerId();
        String targetPlayerId = pickBattleRequestDto.getTargetPlayerId();
        BattleRoundCode battleRoundCode = pickBattleRequestDto.getPickCode();

        Boolean isPickAll = battleService.pickBattle(roomId, playerId, targetPlayerId, battleRoundCode);

        // TODO: 여기부터 디버깅 부터
        if (isPickAll) {
            // 배틀 라운드 진행
            FightBattleDto fightBattleDto = battleService.fightBattle(roomId);

            List<String> topics = fightBattleDto.getBattlePlayers().stream()
                    .map(BattlePlayerVo::getPlayerId)
                    .toList();

            // 결과 값 반환
            FightBattleResponseDto fightBattleResponseDto = FightBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(fightBattleDto.getRound())
                    .battlePlayers(fightBattleDto.getBattlePlayers())
                    .build();

            // 라운드 결과 정보 반환
            BattleResponseDto<FightBattleResponseDto> battleResponseDto = BattleResponseDto.<FightBattleResponseDto>builder()
                    .code(BattleStateCode.BATTLE_FIGHT)
                    .topics(topics)
                    .data(fightBattleResponseDto)
                    .isLastRound(fightBattleDto.getIsLastRound())
                    .build();

            // 마지막 라운드 인 경우 배틀 종료 처리
            if (fightBattleDto.getIsLastRound()) {
                battleService.overBattle(roomId);
            }

            return BattleResponse.BATTLE_FIGHT_BATTLE.toResponseDto(battleResponseDto);
        }

        return BattleResponse.BATTLE_NOT_EXISTS_BATTLE_RESPONSE.toResponseDto(null);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<OverBattleResponseDto>> overBattle(@PathVariable("roomId") Long roomId) {
        // 남은 플레이어 1명 승리로 처리
        List<OverBattleDto> overBattleDtos = battleService.findOverBattle(roomId);

        String winPlayerId = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getPlayerId();
        String winMongCode = overBattleDtos.isEmpty() ? "" : overBattleDtos.get(0).getMongCode();

        OverBattleResponseDto overBattleResponseDto = OverBattleResponseDto.builder()
                .roomId(roomId)
                .winPlayerId(winPlayerId)
                .winMongCode(winMongCode)
                .build();

        return ResponseEntity.ok(BattleResponse.BATTLE_OVER_BATTLE.toResponseDto(overBattleResponseDto));
    }
}
