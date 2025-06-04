package com.monglife.mongs.adapter.in.battle.schedule.worker;

import com.monglife.module.common.logging.annotation.DisableLoggingCascade;
import com.monglife.mongs.application.battle.port.in.QueueUseCase;
import com.monglife.mongs.application.battle.port.in.command.MatchingQueuePlayersCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduleWorker {

    private static final Integer MATCH_PLAYER_COUNT = 2;

    private final QueueUseCase queueUseCase;

    /**
     * 매칭 스케줄
     */
    @DisableLoggingCascade
    public void doMatching() {
        while (true) {
            try {
                queueUseCase.matchingQueuePlayersUseCase(MatchingQueuePlayersCommand.builder()
                        .matchPlayerCount(MATCH_PLAYER_COUNT)
                        .build());
            } catch (Exception e) {
                break;
            }
        }
    }
}
