package com.monglife.mongs.application.battle.port.in.service;

import com.monglife.mongs.application.battle.port.annotation.EndMatch;
import com.monglife.mongs.application.battle.port.exception.NotEndMatchException;
import com.monglife.mongs.application.battle.port.exception.NotExistsMatchException;
import com.monglife.mongs.application.battle.port.in.MatchUseCase;
import com.monglife.mongs.application.battle.port.in.command.*;
import com.monglife.mongs.application.battle.port.in.vo.MatchOutcomeVo;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPick;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchService implements MatchUseCase {

    private final MatchPersistencePort matchPersistencePort;

    private final MatchPublishPort matchPublishPort;

    /**
     * 매치 리워드, 차감 페이 포인트 조회
     */
    @Override
    public MatchOutcomeVo getMatchOutcomeUseCase() {
        return MatchOutcomeVo.builder()
                .exp(Match.getRewardExp())
                .rewardPayPoint(Match.getRewardPayPoint())
                .bettingPayPoint(Match.getBettingPayPoint())
                .build();
    }

    /**
     * 매치 조회
     */
    @Override
    @Transactional
    public Match getMatchUseCase(GetMatchCommand command) {
        return matchPersistencePort.getMatchPort(command.getMatchId())
                .orElseThrow(NotExistsMatchException::new);
    }

    /**
     * 승리 매치 플레이어 조회
     */
    @Override
    @Transactional
    public MatchPlayer getWinMatchPlayerUseCase(GetWinMatchPlayerCommand command) {

        Match match = matchPersistencePort.getMatchPort(command.getMatchId())
                .orElseThrow(NotExistsMatchException::new);

        // 매치 종료 여부 확인
        if (Boolean.FALSE.equals(match.isEnd())) {
            throw new NotEndMatchException();
        }

        return match.getWinner();
    }

    /**
     * 매치 입장
     */
    @Override
    @Transactional
    public Match enterMatchUseCase(EnterMatchCommand command) {

        Match match = matchPersistencePort.getMatchPort(command.getMatchId())
                .orElseThrow(NotExistsMatchException::new);

        if (match.isEnd()) {
            throw new NotExistsMatchException();
        }

        // 플레이어 입장
        match.enterMatchPlayer(command.getPlayerId());

        matchPersistencePort.saveMatchPort(match)
                .orElseThrow(NotExistsMatchException::new);

        // 매치가 시작된 경우 매치 정보 비동기 응답
        if (Boolean.TRUE.equals(match.isStart())) {
            matchPublishPort.publishMatchPort(match);
        }

        return match;
    }

    /**
     * 매치 퇴장
     */
    @Override
    @Transactional
    @EndMatch
    public Match exitMatchUseCase(ExitMatchCommand command) {

        Match match = matchPersistencePort.getMatchPort(command.getMatchId())
                .orElseThrow(NotExistsMatchException::new);

        if (match.isEnd()) {
            throw new NotExistsMatchException();
        }

        // 플레이어 퇴장
        match.exitMatchPlayer(command.getPlayerId());

        // 매치 정보 동기화
        matchPersistencePort.saveMatchPort(match)
                .orElseThrow(NotExistsMatchException::new);

        // 매치가 중단된 경우 승리 매치 종료 비동기 응답
        if (Boolean.TRUE.equals(match.isAllMatchPlayersExited())) {
            matchPublishPort.publishMatchEndPort(match, match.getWinner());
        }

        return match;
    }

    /**
     * 매치 라운드 선택
     */
    @Override
    @Transactional
    @EndMatch
    public Match pickMatchUseCase(PickMatchCommand command) {

        Match match = matchPersistencePort.getMatchPort(command.getMatchId())
                .orElseThrow(NotExistsMatchException::new);

        if (match.isEnd()) {
            throw new NotExistsMatchException();
        }

        // 매치 플레이어 조회
        MatchPlayer matchPlayer = match.getMatchPlayer(command.getPlayerId());
        // 상대 매치 플레이어 조회
        MatchPlayer targetMatchPlayer = match.getMatchPlayer(command.getTargetPlayerId());

        // 매치 선택 도메인 객체 생성
        MatchPick matchPick = switch (command.getPickCode()) {
            case MATCH_PICK_DEFENCE -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(match.getRound())
                    .pickCode(command.getPickCode())
                    .pickValue(matchPlayer.getDefence())
                    .build();
            case MATCH_PICK_HEAL -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(match.getRound())
                    .pickCode(command.getPickCode())
                    .pickValue(matchPlayer.getHeal())
                    .build();
            case MATCH_PICK_ATTACK -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(match.getRound())
                    .pickCode(command.getPickCode())
                    .pickValue(matchPlayer.getAttack())
                    .build();
        };

        // 매치 선택 등록
        boolean isRoundOver = match.pickMatchPlayer(matchPick);

        // 매치 정보 동기화
        matchPersistencePort.saveMatchPort(match)
                .orElseThrow(NotExistsMatchException::new);

        // 다음 라운드 진행한 경우
        if (isRoundOver) {
            // 매치 라운드 종료 비동기 응답
            matchPublishPort.publishMatchPort(match);
        }

        return match;
    }
}
