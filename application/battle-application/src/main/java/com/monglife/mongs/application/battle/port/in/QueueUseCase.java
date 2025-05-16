package com.monglife.mongs.application.battle.port.in;

import com.monglife.mongs.application.battle.port.in.command.CreateQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.DeleteQueuePlayerCommand;
import com.monglife.mongs.application.battle.port.in.command.MatchingQueuePlayersCommand;
import com.monglife.mongs.domain.battle.model.QueuePlayer;

public interface QueueUseCase {

    QueuePlayer createQueuePlayerUseCase(CreateQueuePlayerCommand command);

    QueuePlayer deleteQueuePlayerUseCase(DeleteQueuePlayerCommand command);

    void matchingQueuePlayersUseCase(MatchingQueuePlayersCommand command);
}
