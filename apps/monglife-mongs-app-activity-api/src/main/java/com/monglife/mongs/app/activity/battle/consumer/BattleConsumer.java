package com.monglife.mongs.app.activity.battle.consumer;

import com.monglife.mongs.app.activity.battle.dto.etc.BattleDto;
import com.monglife.mongs.app.activity.battle.dto.etc.OverBattleDto;
import com.monglife.mongs.app.activity.battle.dto.request.EnterBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.ExitBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.request.PickBattleRequestDto;
import com.monglife.mongs.app.activity.battle.dto.response.GetBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.publisher.BattlePublisher;
import com.monglife.mongs.app.activity.battle.service.BattleService;
import com.monglife.mongs.domain.match.enums.MatchRoundCode;
import com.monglife.mongs.module.mqtt.annotation.MqttConsumer;
import com.monglife.mongs.module.mqtt.annotation.MqttMapping;
import com.monglife.mongs.module.mqtt.annotation.MqttPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@MqttConsumer
@RequiredArgsConstructor
public class BattleConsumer {

    private final BattlePublisher battlePublisher;

    private final BattleService battleService;

    /**
     * 배틀룸 입장
     * @param roomId 배틀룸 ID
     * @param enterBattleRequestDto player ID
     */
    @MqttMapping("/battle/enter/{roomId}")
    public void enterBattle(@PathVariable("roomId") Long roomId, @MqttPayload EnterBattleRequestDto enterBattleRequestDto) {

        String playerId = enterBattleRequestDto.getPlayerId();

        battleService.enterBattle(roomId, playerId);
    }

    /**
     * 배틀룸 퇴장
     * @param roomId 배틀룸 ID
     * @param exitBattleRequestDto player ID
     */
    @MqttMapping("/battle/exit/{roomId}")
    public void exitBattle(@PathVariable("roomId") Long roomId, @MqttPayload ExitBattleRequestDto exitBattleRequestDto) {

        String playerId = exitBattleRequestDto.getPlayerId();

        OverBattleDto overBattleDto = battleService.exitBattle(roomId, playerId);

        if (overBattleDto != null) {
        }
    }

    /**
     * 배틀 라운드 선택
     * @param roomId 배틀룸 ID
     * @param pickBattleRequestDto 선택 정보
     */
    @MqttMapping("/battle/pick/{roomId}")
    public void pickBattle(@PathVariable("roomId") Long roomId, @MqttPayload PickBattleRequestDto pickBattleRequestDto) {

        String playerId = pickBattleRequestDto.getPlayerId();
        String targetPlayerId = pickBattleRequestDto.getTargetPlayerId();
        MatchRoundCode matchRoundCode = pickBattleRequestDto.getPickCode();

        BattleDto battleDto = battleService.pickBattle(roomId, playerId, targetPlayerId, matchRoundCode);

        if (battleDto != null) {
            battlePublisher.pickBattlePublish(roomId, GetBattleResponseDto.builder()
                    .roomId(roomId)
                    .round(battleDto.getRound())
                    .battlePlayers(battleDto.getBattlePlayers())
                    .isLastRound(battleDto.getIsLastRound())
                    .build());
        }
    }
}
