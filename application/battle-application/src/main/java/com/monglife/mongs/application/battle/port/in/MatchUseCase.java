package com.monglife.mongs.application.battle.port.in;

import com.monglife.mongs.application.battle.port.in.command.*;
import com.monglife.mongs.application.battle.port.in.vo.MatchOutcomeVo;
import com.monglife.mongs.domain.model.Match;
import com.monglife.mongs.domain.model.MatchPlayer;

public interface MatchUseCase {

    MatchOutcomeVo getMatchOutcomeUseCase();

    Match getMatchUseCase(GetMatchCommand command);

    MatchPlayer getWinMatchPlayerUseCase(GetWinMatchPlayerCommand command);

    Match enterMatchUseCase(EnterMatchCommand command);

    Match exitMatchUseCase(ExitMatchCommand command);

    Match pickMatchUseCase(PickMatchCommand command);
}
