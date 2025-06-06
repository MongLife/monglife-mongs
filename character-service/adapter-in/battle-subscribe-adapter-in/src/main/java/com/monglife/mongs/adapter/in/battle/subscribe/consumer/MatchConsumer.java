package com.monglife.mongs.adapter.in.battle.subscribe.consumer;

import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.mqtt.annotation.MqttConsumer;
import com.monglife.module.mqtt.annotation.MqttMapping;
import com.monglife.module.mqtt.annotation.MqttPayload;
import com.monglife.mongs.adapter.in.battle.subscribe.dto.request.EnterMatchRequestDto;
import com.monglife.mongs.adapter.in.battle.subscribe.dto.request.ExitMatchRequestDto;
import com.monglife.mongs.adapter.in.battle.subscribe.dto.request.PickMatchRequestDto;
import com.monglife.mongs.application.battle.port.in.MatchUseCase;
import com.monglife.mongs.application.battle.port.in.command.EnterMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.ExitMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.PickMatchCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@MqttConsumer
@MqttMapping("/battle")
@RequiredArgsConstructor
public class MatchConsumer {

    private final MatchUseCase matchUseCase;

    /**
     * 매치 입장
     * @param matchId 매치 ID
     * @param enterMatchRequestDto 매치 입장 정보
     */
    @EntryLoggingPoint
    @MqttMapping("/match/enter/{matchId}")
    public void enterMatch(@PathVariable("matchId") Long matchId, @MqttPayload EnterMatchRequestDto enterMatchRequestDto) {

        EnterMatchCommand command = EnterMatchCommand.builder()
                .matchId(matchId)
                .playerId(enterMatchRequestDto.getPlayerId())
                .build();

        matchUseCase.enterMatchUseCase(command);
    }

    /**
     * 매치 퇴장
     * @param matchId 배틀룸 ID
     * @param exitMatchRequestDto 매치 퇴장 정보
     */
    @EntryLoggingPoint
    @MqttMapping("/match/exit/{matchId}")
    public void exitMatch(@PathVariable("matchId") Long matchId, @MqttPayload ExitMatchRequestDto exitMatchRequestDto) {

        ExitMatchCommand command = ExitMatchCommand.builder()
                .matchId(matchId)
                .playerId(exitMatchRequestDto.getPlayerId())
                .build();

        matchUseCase.exitMatchUseCase(command);
    }

    /**
     * 매치 플레이어 라운드 선택
     * @param matchId 매치 ID
     * @param pickMatchRequestDto 선택 정보
     */
    @EntryLoggingPoint
    @MqttMapping("/match/pick/{matchId}")
    public void pickMatch(@PathVariable("matchId") Long matchId, @MqttPayload PickMatchRequestDto pickMatchRequestDto) {

        PickMatchCommand command = PickMatchCommand.builder()
                .matchId(matchId)
                .playerId(pickMatchRequestDto.getPlayerId())
                .targetPlayerId(pickMatchRequestDto.getTargetPlayerId())
                .pickCode(pickMatchRequestDto.getPickCode())
                .build();

        matchUseCase.pickMatchUseCase(command);
    }
}
