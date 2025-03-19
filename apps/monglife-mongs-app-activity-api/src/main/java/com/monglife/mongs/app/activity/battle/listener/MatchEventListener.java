package com.monglife.mongs.app.activity.battle.listener;

import com.monglife.mongs.app.activity.battle.config.BattleProperties;
import com.monglife.mongs.app.activity.battle.dto.response.GetBattleResponseDto;
import com.monglife.mongs.app.activity.battle.dto.response.OverBattleResponseDto;
import com.monglife.mongs.app.activity.battle.publisher.BattlePublisher;
import com.monglife.mongs.client.manager.service.ManagementService;
import com.monglife.mongs.domain.match.dto.event.EnterMatchEvent;
import com.monglife.mongs.domain.match.dto.event.ExitMatchEvent;
import com.monglife.mongs.domain.match.dto.event.NextRoundEvent;
import com.monglife.mongs.domain.match.dto.event.OverMatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEventListener {

    private final BattlePublisher battlePublisher;

    private final ManagementService managementService;

    private final BattleProperties battleProperties;

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
    public void nextRoundEventListener(NextRoundEvent event) {

        battlePublisher.nextRoundPublish(event.getRoomId(), GetBattleResponseDto.builder()
                .roomId(event.getRoomId())
                .round(event.getRound())
                .isLastRound(event.getIsLastRound())
                .battlePlayers(event.getBattlePlayers())
                .build());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void exitMatchEventListener(ExitMatchEvent event) {

        if (!event.getIsBot()) {
            managementService.patchMongAfterBattle(
                    event.getWinMongId(), battleProperties.exp, battleProperties.rewardPayPoint);
        }

        battlePublisher.exitBattlePublish(event.getRoomId(), OverBattleResponseDto.builder()
                .roomId(event.getRoomId())
                .winPlayerId(event.getWinPlayerId())
                .winMongTypeCode(event.getWinMongTypeCode())
                .build());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void overMatchEventListener(OverMatchEvent event) {

        if (!event.getIsBot()) {
            managementService.patchMongAfterBattle(
                    event.getWinMongId(), battleProperties.exp, battleProperties.rewardPayPoint);
        }
    }
}
