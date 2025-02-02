package com.monglife.mongs.app.activity.battle.listener;

import com.monglife.mongs.app.activity.battle.dto.response.CreateBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.GetBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.publisher.BattlePublisher;
import com.monglife.mongs.domain.match.dto.event.CreateMatchEvent;
import com.monglife.mongs.domain.match.dto.event.EnterMatchEvent;
import com.monglife.mongs.domain.match.dto.event.MatchObserveEvent;
import com.monglife.mongs.domain.match.dto.event.OverMatchEvent;
import com.monglife.mongs.domain.match.vo.MatchPlayerVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchEventListener {

    private final BattlePublisher battlePublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createMatchEventListener(CreateMatchEvent event) {

        List<String> deviceIds = event.getMatchPlayers().stream()
                        .map(MatchPlayerVo::getDeviceId)
                        .toList();

        battlePublisher.createBattlePublish(deviceIds, CreateBattleResponseDto.builder()
                .roomId(event.getRoomId())
                .battlePlayers(event.getMatchPlayers())
                .build());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void enterMatchEventListener(EnterMatchEvent event) {

        battlePublisher.enterBattlePublish(event.getRoomId(), GetBattleResponseDto.builder()
                .roomId(event.getRoomId())
                .round(event.getRound())
                .isLastRound(event.getIsLastRound())
                .battlePlayers(event.getBattlePlayers())
                .build());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void matchObserveEventListener(MatchObserveEvent event) {


    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void overMatchEventListener(OverMatchEvent event) {

        battlePublisher.overBattlePublish(event.getRoomId(), OverBattleResponseDto.builder()
                .roomId(event.getRoomId())
                .winPlayerId(event.getWinPlayerId())
                .winMongTypeCode(event.getWinMongTypeCode())
                .build());
    }
}
