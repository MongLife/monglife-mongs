package com.monglife.mongs.domain.battle.utils;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class MatchPlayerTestUtil {

    private static final Random random = new Random();

    /**
     * 봇 매치 플레이어 1명, 사용자 매치 플레이어 1명 / 입장 완료 전 / 매치 시작 전
     * @param matchId 매치 ID
     * @param playerId 플레이어 ID
     * @param deviceId 기기 ID
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param attack 공격 수치
     * @param defence 방어 수치
     * @param heal 회복 수치
     * @param hp 매치 체력
     * @return 매치 도메인 객체
     */
    public static Match generateTwoMatchPlayerBotMatchWhenRound_0(Long matchId, String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {

        List<MatchPlayer> matchPlayers = List.of(
                generateMatchPlayer(playerId, deviceId, accountId, mongId, attack, defence, heal, hp),
                generateBotMatchPlayer(attack, defence, heal, hp)
        );

        return Match.builder()
                .matchId(matchId)
                .round(0)
                .maxRound(Match.getInitMaxRound())
                .matchPlayers(matchPlayers)
                .build();
    }

    /**
     * 봇 매치 플레이어 1명, 사용자 매치 플레이어 1명 / 입장 완료 / 매치 시작
     * @param matchId 매치 ID
     * @param playerId 플레이어 ID
     * @param deviceId 기기 ID
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param attack 공격 수치
     * @param defence 방어 수치
     * @param heal 회복 수치
     * @param hp 매치 체력
     * @return 매치 도메인 객체
     */
    public static Match generateTwoMatchPlayerBotMatchWhenRound_1(Long matchId, String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {

        List<MatchPlayer> matchPlayers = List.of(
                generateMatchPlayer(playerId, deviceId, accountId, mongId, attack, defence, heal, hp),
                generateBotMatchPlayer(attack, defence, heal, hp)
        );

        Match match = Match.builder()
                .matchId(matchId)
                .round(1)
                .maxRound(Match.getInitMaxRound())
                .matchPlayers(matchPlayers)
                .build();

        match.enterMatchPlayer(playerId);

        return match;
    }

    /**
     * 봇 매치 플레이어 1명, 사용자 매치 플레이어 1명 / 입장 완료 / 마지막 매치 바로 전 라운드
     * @param matchId 매치 ID
     * @param playerId 플레이어 ID
     * @param deviceId 기기 ID
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param attack 공격 수치
     * @param defence 방어 수치
     * @param heal 회복 수치
     * @param hp 매치 체력
     * @return 매치 도메인 객체
     */
    public static Match generateTwoMatchPlayerBotMatchWhenMaxRound(Long matchId, String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {

        MatchPlayer matchPlayer = generateMatchPlayer(playerId, deviceId, accountId, mongId, attack, defence, heal, hp);
        MatchPlayer botMatchPlayer = generateBotMatchPlayer(attack, defence, heal, hp);

        List<MatchPlayer> matchPlayers = List.of(matchPlayer, botMatchPlayer);

        matchPlayer.enter();

        return Match.builder()
                .matchId(matchId)
                .round(Match.getInitMaxRound())
                .maxRound(Match.getInitMaxRound())
                .matchPlayers(matchPlayers)
                .stateCode(MatchStateCode.PROCESS)
                .build();
    }

    /**
     * 사용자 매치 플레이어 2명 / 입장 완료 전 / 매치 시작 전
     * @param matchId 매치 ID
     * @param playerId 플레이어 ID
     * @param deviceId 기기 ID
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param attack 공격 수치
     * @param defence 방어 수치
     * @param heal 회복 수치
     * @param hp 매치 체력
     * @return 매치 도메인 객체
     */
    public static Match generateTwoMatchPlayerMatchWhenRound_0(Long matchId, String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {

        List<MatchPlayer> matchPlayers = List.of(
                generateMatchPlayer(playerId, deviceId, accountId, mongId, attack, defence, heal, hp),
                generateMatchPlayer(accountId + 1, mongId + 1, attack, defence, heal, hp)
        );

        return Match.builder()
                .matchId(matchId)
                .round(0)
                .maxRound(Match.getInitMaxRound())
                .matchPlayers(matchPlayers)
                .build();
    }

    /**
     * 사용자 매치 플레이어 2명 / 입장 완료 / 매치 시작
     * @param matchId 매치 ID
     * @param playerId 플레이어 ID
     * @param deviceId 기기 ID
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param attack 공격 수치
     * @param defence 방어 수치
     * @param heal 회복 수치
     * @param hp 매치 체력
     * @return 매치 도메인 객체
     */
    public static Match generateTwoMatchPlayerMatchWhenRound_1(Long matchId, String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {

        List<MatchPlayer> matchPlayers = List.of(
                generateMatchPlayer(playerId, deviceId, accountId, mongId, attack, defence, heal, hp),
                generateMatchPlayer(accountId + 1, mongId + 1, attack, defence, heal, hp)
        );

        Match match = Match.builder()
                .matchId(matchId)
                .round(1)
                .maxRound(Match.getInitMaxRound())
                .matchPlayers(matchPlayers)
                .build();

        matchPlayers.forEach(matchPlayer -> match.enterMatchPlayer(matchPlayer.getPlayerId()));

        return match;
    }

    /**
     * 사용자 매치 플레이어 3명 / 입장 완료 / 매치 시작
     * @param matchId 매치 ID
     * @param playerId 플레이어 ID
     * @param deviceId 기기 ID
     * @param accountId 계정 ID
     * @param mongId 몽 ID
     * @param attack 공격 수치
     * @param defence 방어 수치
     * @param heal 회복 수치
     * @param hp 매치 체력
     * @return 매치 도메인 객체
     */
    public static Match generateThreeMatchPlayerMatchWhenRound_1(Long matchId, String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {

        List<MatchPlayer> matchPlayers = List.of(
                generateMatchPlayer(playerId, deviceId, accountId, mongId, attack, defence, heal, hp),
                generateMatchPlayer(accountId + 1, mongId + 1, attack, defence, heal, hp),
                generateMatchPlayer(accountId + 2, mongId + 2, attack, defence, heal, hp)
        );

        Match match = Match.builder()
                .matchId(matchId)
                .round(1)
                .maxRound(Match.getInitMaxRound())
                .matchPlayers(matchPlayers)
                .build();

        matchPlayers.forEach(matchPlayer -> match.enterMatchPlayer(matchPlayer.getPlayerId()));

        return match;
    }

    private static MatchPlayer generateMatchPlayer(Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {
        return MatchPlayer.builder()
                .playerId(CommonUtil.randomId())
                .deviceId(CommonUtil.randomId())
                .accountId(accountId)
                .mongId(mongId)
                .mongCode("TEST-MONG-TYPE-CODE")
                .mongName("TEST-MONG-TYPE-NAME")
                .name("테스트 몽 이름")
                .attack(attack)
                .heal(heal)
                .defence(defence)
                .isBot(false)
                .hp(hp)
                .isEnter(false)
                .build();
    }

    private static MatchPlayer generateMatchPlayer(String playerId, String deviceId, Long accountId, Long mongId, Double attack, Double defence, Double heal, Double hp) {
        return MatchPlayer.builder()
                .playerId(playerId)
                .deviceId(deviceId)
                .accountId(accountId)
                .mongId(mongId)
                .mongCode("TEST-MONG-TYPE-CODE")
                .mongName("TEST-MONG-TYPE-NAME")
                .name("테스트 몽 이름")
                .attack(attack)
                .heal(heal)
                .defence(defence)
                .isBot(false)
                .hp(hp)
                .isEnter(false)
                .build();
    }

    private static MatchPlayer generateBotMatchPlayer(Double attack, Double defence, Double heal, Double hp) {
        return MatchPlayer.builder()
                .playerId(CommonUtil.randomId())
                .deviceId(CommonUtil.randomId())
                .accountId(-1L)
                .mongId(-1L)
                .mongCode("TEST-MONG-TYPE-CODE")
                .mongName("TEST-MONG-TYPE-NAME")
                .name("봇 테스트 몽 이름")
                .attack(attack)
                .heal(heal)
                .defence(defence)
                .isBot(true)
                .hp(hp)
                .isEnter(true)
                .enteredAt(LocalDateTime.now())
                .build();
    }

    /**
     * 상대 매치 플레이어 랜덤 조회
     * @param match 매치 도메인 객체
     * @param matchPlayer 제외할 매치 플레이어 도메인 객체
     * @return 랜덤 상대 매치 플레이어 도메인 객체
     */
    public static MatchPlayer getRandomTargetMatchPlayer(Match match, MatchPlayer matchPlayer) {

        List<MatchPlayer> targetMatchPlayers = match.getMatchPlayers().stream()
                .filter(targetMatchPlayer -> !targetMatchPlayer.getPlayerId().equals(matchPlayer.getPlayerId()))
                .toList();

        int targetMatchPlayerIndex = random.nextInt(0, targetMatchPlayers.size());
        return targetMatchPlayers.get(targetMatchPlayerIndex);
    }
}
