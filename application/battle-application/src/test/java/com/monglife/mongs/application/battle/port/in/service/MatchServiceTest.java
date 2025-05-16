package com.monglife.mongs.application.battle.port.in.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.application.battle.port.exception.AlreadyExistsMatchPickException;
import com.monglife.mongs.application.battle.port.in.MatchUseCase;
import com.monglife.mongs.application.battle.port.in.command.EnterMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.ExitMatchCommand;
import com.monglife.mongs.application.battle.port.in.command.PickMatchCommand;
import com.monglife.mongs.application.battle.port.out.MatchPersistencePort;
import com.monglife.mongs.application.battle.port.out.MatchPublishPort;
import com.monglife.mongs.domain.enums.MatchPickCode;
import com.monglife.mongs.domain.enums.MatchRoundCode;
import com.monglife.mongs.domain.model.Match;
import com.monglife.mongs.domain.model.MatchPick;
import com.monglife.mongs.domain.model.MatchPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

            Match expected = matchUseCase.enterMatchUseCase(command);

            // assert
            assertTrue(expected.isStart());
            assertTrue(expected.getMatchPlayer(PLAYER_ID).getIsEnter());
            assertNotNull(expected.getMatchPlayer(PLAYER_ID).getEnteredAt());
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

            Match expected = matchUseCase.enterMatchUseCase(command);

            // assert
            assertFalse(expected.isStart());
            assertTrue(expected.getMatchPlayer(PLAYER_ID).getIsEnter());
            assertNotNull(expected.getMatchPlayer(PLAYER_ID).getEnteredAt());
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

            Match expected = matchUseCase.exitMatchUseCase(command);

            // assert
            assertTrue(expected.isEnd());
            assertFalse(expected.getMatchPlayer(PLAYER_ID).getIsEnter());
            assertNotNull(expected.getMatchPlayer(PLAYER_ID).getExitedAt());
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

            Match expected = matchUseCase.exitMatchUseCase(command);

            // assert
            assertFalse(expected.isEnd());
            assertFalse(expected.getMatchPlayer(PLAYER_ID).getIsEnter());
            assertNotNull(expected.getMatchPlayer(PLAYER_ID).getExitedAt());
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

            Match expected = matchUseCase.pickMatchUseCase(command);

            // assert
            Mockito.verify(matchPublishPort).publishMatch(match);
            assertTrue(pastRound < expected.getRound());
            assertTrue(HP > expected.getMatchPlayer(targetMatchPlayer.getPlayerId()).getHp());
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

            Match expected = matchUseCase.pickMatchUseCase(command);

            // assert
            assertEquals(pastRound, expected.getRound());
        }

        @Test
        @DisplayName("해당 라운드에서 이미 선택한 경우 예외가 발생 한다.")
        void pickMatchWhenAlreadyPicked() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            match.pickMatchPlayer(MatchPick.builder()
                    .matchPlayer(match.getMatchPlayer(PLAYER_ID))
                    .targetMatchPlayer(match.getMatchPlayer(PLAYER_ID))
                    .round(match.getRound())
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .value(100D)
                    .build());

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act & assert
            PickMatchCommand command = PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .targetPlayerId(PLAYER_ID)
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .build();

            assertThrows(AlreadyExistsMatchPickException.class, () -> matchUseCase.pickMatchUseCase(command));
        }

        @Test
        @DisplayName("마지막 라운드인 경우 매치가 종료 된다.")
        void pickMatchWhenLastRound() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenMaxRound(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);
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

            Match expected = matchUseCase.pickMatchUseCase(command);
            MatchPlayer winMatchPlayer = expected.getWinner();

            // assert
            assertTrue(expected.isEnd());
            assertEquals(pastRound, expected.getRound());
            assertEquals(PLAYER_ID, winMatchPlayer.getPlayerId());
        }

        @Test
        @DisplayName("매치 플레이어 중 사망한 플레이어가 발생하는 경우 매치가 종료 된다.")
        void pickMatchWhenMatchPlayerDead() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            // 빈사 상태로 변경
            targetMatchPlayer.damage(HP - 1);
            targetMatchPlayer.applyDamageAndRecovery();

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            PickMatchCommand command = PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(PLAYER_ID)
                    .targetPlayerId(targetMatchPlayer.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .build();

            Match expected = matchUseCase.pickMatchUseCase(command);
            MatchPlayer winMatchPlayer = expected.getWinner();

            // assert
            assertTrue(expected.isEnd());
            assertTrue(match.getRound() < Match.getMaxRound());
            assertEquals(PLAYER_ID, winMatchPlayer.getPlayerId());
        }

        @Test
        @DisplayName("[매치 플레이어 1 : 방어, 매치 플레이어 2 : 방어 ] 인 경우 [ 플레이어 1, 플레이어 2 ] 는 방어 한다.")
        void pickMatchWhenDefenceAndDefence() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
            MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            // 플레이어 1 매치 선택
            matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer1.getPlayerId())
                    .targetPlayerId(matchPlayer1.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                    .build());
            // 플레이어 2 매치 선택
            Match expected = matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer2.getPlayerId())
                    .targetPlayerId(matchPlayer2.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                    .build());
            MatchPlayer expected1 = expected.getMatchPlayer(matchPlayer1.getPlayerId());
            MatchPlayer expected2 = expected.getMatchPlayer(matchPlayer2.getPlayerId());

            // assert
            assertEquals(MatchRoundCode.MATCH_DEFENCE, expected1.getMatchRoundCode());
            assertEquals(MatchRoundCode.MATCH_DEFENCE, expected2.getMatchRoundCode());
            assertEquals(HP, expected1.getHp());
            assertEquals(HP, expected2.getHp());
        }

        @Test
        @DisplayName("[매치 플레이어 1 : 공격, 매치 플레이어 2 : 공격 ] 인 경우 [ 플레이어 1, 플레이어 2 ] 는 피해를 입는다.")
        void pickMatchWhenAttackAndAttack() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
            MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            // 플레이어 1 매치 선택
            matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer1.getPlayerId())
                    .targetPlayerId(matchPlayer2.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .build());
            // 플레이어 2 매치 선택
            Match expected = matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer2.getPlayerId())
                    .targetPlayerId(matchPlayer1.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .build());
            MatchPlayer expected1 = expected.getMatchPlayer(matchPlayer1.getPlayerId());
            MatchPlayer expected2 = expected.getMatchPlayer(matchPlayer2.getPlayerId());

            // assert
            assertEquals(MatchRoundCode.MATCH_ATTACKED, expected1.getMatchRoundCode());
            assertEquals(MatchRoundCode.MATCH_ATTACKED, expected2.getMatchRoundCode());
            assertTrue(HP > expected1.getHp());
            assertTrue(HP > expected2.getHp());
        }

        @Test
        @DisplayName("[매치 플레이어 1 : 회복, 매치 플레이어 2 : 회복 ] 인 경우 [ 플레이어 1, 플레이어 2 ] 는 회복 한다.")
        void pickMatchWhenHealAndHeal() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP - HEAL);

            MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
            MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            // 플레이어 1 매치 선택
            matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer1.getPlayerId())
                    .targetPlayerId(matchPlayer1.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .build());
            // 플레이어 2 매치 선택
            Match expected = matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer2.getPlayerId())
                    .targetPlayerId(matchPlayer2.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .build());
            System.out.println(matchPlayer1);
            System.out.println(matchPlayer2);
            MatchPlayer expected1 = expected.getMatchPlayer(matchPlayer1.getPlayerId());
            MatchPlayer expected2 = expected.getMatchPlayer(matchPlayer2.getPlayerId());

            // assert
            assertEquals(MatchRoundCode.MATCH_HEAL, expected1.getMatchRoundCode());
            assertEquals(MatchRoundCode.MATCH_HEAL, expected2.getMatchRoundCode());
            assertEquals(HP, expected1.getHp());
            assertEquals(HP, expected2.getHp());
        }

        @Test
        @DisplayName("[매치 플레이어 1 : 회복, 매치 플레이어 2 : 방어 ] 인 경우 [ 플레이어 1 ] 는 회복, [ 플레이어 2 ] 는 방어 한다.")
        void pickMatchWhenHealAndDefence() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP - HEAL);

            MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
            MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            // 플레이어 1 매치 선택
            matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer1.getPlayerId())
                    .targetPlayerId(matchPlayer1.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .build());
            // 플레이어 2 매치 선택
            Match expected = matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer2.getPlayerId())
                    .targetPlayerId(matchPlayer2.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                    .build());
            MatchPlayer expected1 = expected.getMatchPlayer(matchPlayer1.getPlayerId());
            MatchPlayer expected2 = expected.getMatchPlayer(matchPlayer2.getPlayerId());

            // assert
            assertEquals(MatchRoundCode.MATCH_HEAL, expected1.getMatchRoundCode());
            assertEquals(MatchRoundCode.MATCH_DEFENCE, expected2.getMatchRoundCode());
            assertEquals(HP, expected1.getHp());
            assertEquals(HP - HEAL, expected2.getHp());
        }

        @Test
        @DisplayName("[매치 플레이어 1 : 회복, 매치 플레이어 2 : 공격 ] 인 경우 [ 플레이어 1 ] 는 회복과 동시에 피해를 입는다.")
        void pickMatchWhenHealAndAttack() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
            MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            // 플레이어 1 매치 선택
            matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer1.getPlayerId())
                    .targetPlayerId(matchPlayer1.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                    .build());
            // 플레이어 2 매치 선택
            Match expected = matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer2.getPlayerId())
                    .targetPlayerId(matchPlayer1.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .build());
            MatchPlayer expected1 = expected.getMatchPlayer(matchPlayer1.getPlayerId());
            MatchPlayer expected2 = expected.getMatchPlayer(matchPlayer2.getPlayerId());

            // assert
            assertEquals(MatchRoundCode.MATCH_ATTACKED_HEAL, expected1.getMatchRoundCode());
            assertEquals(MatchRoundCode.NONE, expected2.getMatchRoundCode());
            assertTrue(HP > expected1.getHp());
            assertEquals(HP, expected2.getHp());
        }

        @Test
        @DisplayName("[매치 플레이어 1 : 공격, 매치 플레이어 2 : 방어 ] 인 경우 [ 플레이어 2 ] 는 방어와 동시에 피해를 입는다.")
        void pickMatchWhenAttackAndDefence() {
            // arrange
            Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

            MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
            MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

            Mockito.when(matchPersistencePort.getMatchPort(MATCH_ID)).thenReturn(Optional.of(match));
            Mockito.when(matchPersistencePort.saveMatchPort(match)).thenReturn(Optional.of(match));

            // act
            // 플레이어 1 매치 선택
            matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer1.getPlayerId())
                    .targetPlayerId(matchPlayer2.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                    .build());
            // 플레이어 2 매치 선택
            Match expected = matchUseCase.pickMatchUseCase(PickMatchCommand.builder()
                    .matchId(MATCH_ID)
                    .playerId(matchPlayer2.getPlayerId())
                    .targetPlayerId(matchPlayer2.getPlayerId())
                    .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                    .build());
            MatchPlayer expected1 = expected.getMatchPlayer(matchPlayer1.getPlayerId());
            MatchPlayer expected2 = expected.getMatchPlayer(matchPlayer2.getPlayerId());

            // assert
            assertEquals(MatchRoundCode.NONE, expected1.getMatchRoundCode());
            assertEquals(MatchRoundCode.MATCH_DEFENCE, expected2.getMatchRoundCode());
            assertEquals(HP, expected1.getHp());
            assertTrue(HP > expected2.getHp());
        }
    }
}