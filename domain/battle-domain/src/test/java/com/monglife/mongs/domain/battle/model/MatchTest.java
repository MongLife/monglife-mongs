package com.monglife.mongs.domain.battle.model;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import com.monglife.mongs.domain.battle.enums.MatchRoundCode;
import com.monglife.mongs.domain.battle.exception.AlreadyExistsMatchPickException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private static final Long MATCH_ID = 1L;
    private static final String PLAYER_ID = CommonUtil.randomId();
    private static final String DEVICE_ID = CommonUtil.randomId();
    private static final Long ACCOUNT_ID = 1L;
    private static final Long MONG_ID = 1L;
    private static final Double ATTACK = 50D;
    private static final Double DEFENSE = 25D;
    private static final Double HEAL = 10D;
    private static final Double HP = 100D;

    @Test
    @DisplayName("매치 입장 시 모든 매치 플레이어가 입장한 경우 매치가 시작 된다.")
    void enterMath() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_0(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        // act
        match.enterMatchPlayer(PLAYER_ID);

        // assert
        assertTrue(match.isStart());
        assertTrue(match.getMatchPlayer(PLAYER_ID).getIsEnter());
        assertNotNull(match.getMatchPlayer(PLAYER_ID).getEnteredAt());
    }

    @Test
    @DisplayName("매치 입장 시 모든 매치 플레이어가 입장하지 않은 경우 매치가 시작 되지 않는다.")
    void enterMatchWhenNotAllMatchPlayerEntered() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_0(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        // act
        match.enterMatchPlayer(PLAYER_ID);

        // assert
        assertFalse(match.isStart());
        assertTrue(match.getMatchPlayer(PLAYER_ID).getIsEnter());
        assertNotNull(match.getMatchPlayer(PLAYER_ID).getEnteredAt());
    }

    @Test
    @DisplayName("매치 퇴장 시 모든 매치 플레이어 퇴장 또는 1명의 매치 플레이어만 잔류한 경우 매치를 종료 한다.")
    void exitMatch() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        // act
        match.exitMatchPlayer(PLAYER_ID);

        // assert
        assertTrue(match.isEnd());
        assertFalse(match.getMatchPlayer(PLAYER_ID).getIsEnter());
        assertNotNull(match.getMatchPlayer(PLAYER_ID).getExitedAt());
    }

    @Test
    @DisplayName("매치 퇴장 시 2명 이상의 매치 플레이어가 잔류한 경우 매치를 종료 하지 않는다.")
    void exitMatchWhenNotAllMatchPlayerExited() {
        // arrange
        Match match = MatchPlayerTestUtil.generateThreeMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        // act
        match.exitMatchPlayer(PLAYER_ID);

        // assert
        assertFalse(match.isEnd());
        assertFalse(match.getMatchPlayer(PLAYER_ID).getIsEnter());
        assertNotNull(match.getMatchPlayer(PLAYER_ID).getExitedAt());
    }

    @Test
    @DisplayName("모든 플레이어가 매치 라운드 선택을 완료한 경우 다음 라운드로 진행하고, 선택 값들을 매치 플레이어에게 적용 한다.")
    void pickMatch() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);
        int pastRound = match.getRound();

        MatchPlayer matchPlayer = match.getMatchPlayer(PLAYER_ID);
        MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

        // act
        MatchPick matchPick = MatchPick.builder()
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(match.getRound())
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .value(matchPlayer.getAttack())
                .build();

        match.pickMatchPlayer(matchPick);

        // assert
        assertTrue(pastRound < match.getRound());
        assertTrue(HP > match.getMatchPlayer(targetMatchPlayer.getPlayerId()).getHp());
    }

    @Test
    @DisplayName("모든 플레이어가 매치 라운드 선택을 완료하지 않은 경우 다음 라운드로 진행되지 않는다.")
    void pickMatchWhenNotAllMatchPlayerPicked() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);
        int pastRound = match.getRound();

        MatchPlayer matchPlayer = match.getMatchPlayer(PLAYER_ID);
        MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

        // act
        MatchPick matchPick = MatchPick.builder()
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(match.getRound())
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .value(matchPlayer.getAttack())
                .build();

        match.pickMatchPlayer(matchPick);

        // assert
        assertEquals(pastRound, match.getRound());
    }

    @Test
    @DisplayName("해당 라운드에서 이미 선택한 경우 예외가 발생 한다.")
    void pickMatchWhenAlreadyPicked() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        MatchPlayer matchPlayer = match.getMatchPlayer(PLAYER_ID);
        MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(match.getRound())
                .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                .value(matchPlayer.getHeal())
                .build());

        // act & assert
        MatchPick matchPick = MatchPick.builder()
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(match.getRound())
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .value(matchPlayer.getAttack())
                .build();

        assertThrows(AlreadyExistsMatchPickException.class, () -> match.pickMatchPlayer(matchPick));
    }

    @Test
    @DisplayName("마지막 라운드인 경우 매치가 종료 된다.")
    void pickMatchWhenLastRound() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenMaxRound(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);
        int pastRound = match.getRound();

        MatchPlayer matchPlayer = match.getMatchPlayer(PLAYER_ID);
        MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

        // act
        MatchPick matchPick = MatchPick.builder()
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(match.getRound())
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .value(matchPlayer.getAttack())
                .build();

        match.pickMatchPlayer(matchPick);
        MatchPlayer winMatchPlayer = match.getWinner();

        // assert
        assertTrue(match.isEnd());
        assertEquals(pastRound, match.getRound());
        assertEquals(PLAYER_ID, winMatchPlayer.getPlayerId());
    }

    @Test
    @DisplayName("매치 플레이어 중 사망한 플레이어가 발생하는 경우 매치가 종료 된다.")
    void pickMatchWhenMatchPlayerDead() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerBotMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        MatchPlayer matchPlayer = match.getMatchPlayer(PLAYER_ID);
        MatchPlayer targetMatchPlayer = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

        // 빈사 상태로 변경
        targetMatchPlayer.damage(HP - 1);
        targetMatchPlayer.applyDamageAndRecovery();

        // act
        MatchPick matchPick = MatchPick.builder()
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(match.getRound())
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .value(matchPlayer.getAttack())
                .build();

        match.pickMatchPlayer(matchPick);
        MatchPlayer winMatchPlayer = match.getWinner();

        // assert
        assertTrue(match.isEnd());
        assertTrue(match.getRound() < Match.getInitMaxRound());
        assertEquals(PLAYER_ID, winMatchPlayer.getPlayerId());
    }

    @Test
    @DisplayName("[매치 플레이어 1 : 방어, 매치 플레이어 2 : 방어 ] 인 경우 [ 플레이어 1, 플레이어 2 ] 는 방어 한다.")
    void pickMatchWhenDefenceAndDefence() {
        // arrange
        Match match = MatchPlayerTestUtil.generateTwoMatchPlayerMatchWhenRound_1(MATCH_ID, PLAYER_ID, DEVICE_ID, ACCOUNT_ID, MONG_ID, ATTACK, DEFENSE, HEAL, HP);

        MatchPlayer matchPlayer1 = match.getMatchPlayer(PLAYER_ID);
        MatchPlayer matchPlayer2 = MatchPlayerTestUtil.getRandomTargetMatchPlayer(match, match.getMatchPlayer(PLAYER_ID));

        // act
        // 플레이어 1 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer1)
                .targetMatchPlayer(matchPlayer1)
                .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                .round(match.getRound())
                .value(matchPlayer1.getDefence())
                .build());
        // 플레이어 2 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer2)
                .targetMatchPlayer(matchPlayer2)
                .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                .round(match.getRound())
                .value(matchPlayer2.getDefence())
                .build());

        MatchPlayer expected1 = match.getMatchPlayer(matchPlayer1.getPlayerId());
        MatchPlayer expected2 = match.getMatchPlayer(matchPlayer2.getPlayerId());

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

        // act
        // 플레이어 1 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer1)
                .targetMatchPlayer(matchPlayer2)
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .round(match.getRound())
                .value(matchPlayer1.getAttack())
                .build());
        // 플레이어 2 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer2)
                .targetMatchPlayer(matchPlayer1)
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .round(match.getRound())
                .value(matchPlayer2.getAttack())
                .build());

        MatchPlayer expected1 = match.getMatchPlayer(matchPlayer1.getPlayerId());
        MatchPlayer expected2 = match.getMatchPlayer(matchPlayer2.getPlayerId());

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

        // act
        // 플레이어 1 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer1)
                .targetMatchPlayer(matchPlayer1)
                .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                .round(match.getRound())
                .value(matchPlayer1.getHeal())
                .build());
        // 플레이어 2 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer2)
                .targetMatchPlayer(matchPlayer2)
                .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                .round(match.getRound())
                .value(matchPlayer2.getHeal())
                .build());

        MatchPlayer expected1 = match.getMatchPlayer(matchPlayer1.getPlayerId());
        MatchPlayer expected2 = match.getMatchPlayer(matchPlayer2.getPlayerId());

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

        // act
        // 플레이어 1 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer1)
                .targetMatchPlayer(matchPlayer1)
                .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                .round(match.getRound())
                .value(matchPlayer1.getHeal())
                .build());
        // 플레이어 2 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer2)
                .targetMatchPlayer(matchPlayer2)
                .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                .round(match.getRound())
                .value(matchPlayer2.getDefence())
                .build());

        MatchPlayer expected1 = match.getMatchPlayer(matchPlayer1.getPlayerId());
        MatchPlayer expected2 = match.getMatchPlayer(matchPlayer2.getPlayerId());

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

        // act
        // 플레이어 1 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer1)
                .targetMatchPlayer(matchPlayer1)
                .matchPickCode(MatchPickCode.MATCH_PICK_HEAL)
                .round(match.getRound())
                .value(matchPlayer1.getHeal())
                .build());
        // 플레이어 2 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer2)
                .targetMatchPlayer(matchPlayer1)
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .round(match.getRound())
                .value(matchPlayer2.getAttack())
                .build());

        MatchPlayer expected1 = match.getMatchPlayer(matchPlayer1.getPlayerId());
        MatchPlayer expected2 = match.getMatchPlayer(matchPlayer2.getPlayerId());

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

        // act
        // 플레이어 1 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer1)
                .targetMatchPlayer(matchPlayer2)
                .matchPickCode(MatchPickCode.MATCH_PICK_ATTACK)
                .round(match.getRound())
                .value(matchPlayer1.getAttack())
                .build());
        // 플레이어 2 매치 선택
        match.pickMatchPlayer(MatchPick.builder()
                .matchPlayer(matchPlayer2)
                .targetMatchPlayer(matchPlayer2)
                .matchPickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                .round(match.getRound())
                .value(matchPlayer2.getDefence())
                .build());

        MatchPlayer expected1 = match.getMatchPlayer(matchPlayer1.getPlayerId());
        MatchPlayer expected2 = match.getMatchPlayer(matchPlayer2.getPlayerId());

        // assert
        assertEquals(MatchRoundCode.NONE, expected1.getMatchRoundCode());
        assertEquals(MatchRoundCode.MATCH_DEFENCE, expected2.getMatchRoundCode());
        assertEquals(HP, expected1.getHp());
        assertTrue(HP > expected2.getHp());
    }
}