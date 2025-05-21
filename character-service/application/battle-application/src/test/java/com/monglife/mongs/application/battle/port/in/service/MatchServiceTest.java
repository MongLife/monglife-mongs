package com.monglife.mongs.application.battle.port.in.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.application.battle.port.in.MatchUseCase;
import com.monglife.mongs.application.battle.port.in.command.EnterMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.ExitMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.PickMatchCommand;
import com.monglife.mongs.application.battle.port.in.utils.MatchPlayerTestUtil;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchServiceTest {

    private final MatchPersistencePort matchPersistencePort;

    private final MatchPublishPort matchPublishPort;

    private final MatchUseCase matchUseCase;

    public MatchServiceTest() {
        this.matchPersistencePort = Mockito.mock(MatchPersistencePort.class);
        this.matchPublishPort = Mockito.mock(MatchPublishPort.class);
        this.matchUseCase = new MatchService(matchPersistencePort, matchPublishPort);
    }

    private static final Long MATCH_ID = 1L;
    private static final String PLAYER_ID = CommonUtil.randomId();
    private static final String DEVICE_ID = CommonUtil.randomId();
    private static final Long ACCOUNT_ID = 1L;
    private static final Long MONG_ID = 1L;
    private static final Double ATTACK = 50D;
    private static final Double DEFENSE = 25D;
    private static final Double HEAL = 10D;
    private static final Double HP = 100D;

    @Nested
    @DisplayName("매치 입장 단위 테스트")
    class EnterMatchUseCase {

        @Test
        @DisplayName("매치 입장 시 모든 매치 플레이어가 입장한 경우 매치가 시작 된다.")
        void enterMatch() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_0(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            EnterMatchCommand command = EnterMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .build();

            matchUseCase.enterMatchUseCase(command);

            // assert
            Mockito.verify(matchPublishPort).publishMatch(match);
        }

        @Test
        @DisplayName("매치 입장 시 모든 매치 플레이어가 입장하지 않은 경우 매치가 시작 되지 않는다.")
        void enterMatchWhenNotAllMatchPlayerEntered() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_0(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            EnterMatchCommand command = EnterMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .build();

            matchUseCase.enterMatchUseCase(command);

            // assert
            Mockito.verify(matchPublishPort, Mockito.never()).publishMatch(match);
        }
    }

    @Nested
    @DisplayName("매치 퇴장 단위 테스트")
    class ExitMatchUseCase {

        @Test
        @DisplayName("매치 퇴장 시 모든 매치 플레이어 퇴장 또는 1명의 매치 플레이어만 잔류한 경우 매치를 종료 한다.")
        void exitMatch() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            ExitMatchCommand command = ExitMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .build();

            matchUseCase.exitMatchUseCase(command);

            // assert
            Mockito.verify(matchPublishPort).publishStopMatch(match);
        }

        @Test
        @DisplayName("매치 퇴장 시 2명 이상의 매치 플레이어가 잔류한 경우 매치를 종료 하지 않는다.")
        void exitMatchWhenNotAllMatchPlayerExited() {
            // arrange
            Match match = MatchPlayerTestUtil.generateThreeMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            ExitMatchCommand command = ExitMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .build();

            matchUseCase.exitMatchUseCase(command);

            // assert
            Mockito.verify(matchPublishPort, Mockito.never()).publishStopMatch(match);
        }
    }

    @Nested
    @DisplayName("매치 라운드 선택 단위 테스트")
    class PickMatchUseCase {

        @Test
        @DisplayName("모든 플레이어가 매치 라운드 선택을 완료한 경우 다음 라운드로 진행하고, 선택 값들을 매치 플레이어에게 적용 한다.")
        void pickMatch() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);
            int pastRound = match.getRound();

            MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            PickMatchCommand command = PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .targetPlayerId(targetMatchPlayer.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .build();

            var expected = matchUseCase.pickMatchUseCase(command);

            // assert
            assertTrue(pastRound < expected.getRound());
            Mockito.verify(matchPublishPort).publishMatch(match);
        }

        @Test
        @DisplayName("모든 플레이어가 매치 라운드 선택을 완료하지 않은 경우 다음 라운드로 진행되지 않는다.")
        void pickMatchWhenNotAllMatchPlayerPicked() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);
            int pastRound = match.getRound();

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            PickMatchCommand command = PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .targetPlayerId(PLAYER_ID)
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .build();

            var expected = matchUseCase.pickMatchUseCase(command);

            // assert
            assertEquals(pastRound, expected.getRound());
            Mockito.verify(matchPublishPort, Mockito.never()).publishMatch(match);
        }
    }
}