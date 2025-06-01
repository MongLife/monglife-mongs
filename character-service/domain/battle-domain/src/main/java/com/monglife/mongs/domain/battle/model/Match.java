package com.monglife.mongs.domain.battle.model;

import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.exception.AlreadyExistsMatchPickException;
import com.monglife.mongs.domain.battle.exception.AlreadyStartMatchException;
import com.monglife.mongs.domain.battle.exception.NotExistsMatchPlayerException;
import com.monglife.mongs.domain.battle.exception.NotPickedAllMatchPlayersException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
public class Match {

    // 매치 완료 시 보상 경험치
    private static final double REWARD_EXP = 10D;
    // 매치 승리 시 보상 페이 포인트
    private static final int REWARD_PAY_POINT = 200;
    // 매치 참여 시 배팅 페이 포인트
    private static final int BETTING_PAY_POINT = 50;
    // 매치 생성 시, 초기 라운드 수
    private static final int INIT_ROUND = 0;
    // 매치 생성 시, 초기 상태 코드
    private static final MatchStateCode INIT_MATCH_STATE_CODE = MatchStateCode.ENTERING;
    // 매치 최대 라운드 수
    private static final int MAX_ROUND = 10;

    private final Long matchId;

    private final Integer maxRound;

    private final List<MatchPlayer> matchPlayers;

    private final List<MatchPick> matchPicks;

    private Integer round;

    private MatchStateCode stateCode;

    @Builder
    public Match(Long matchId, Integer maxRound, Integer round, MatchStateCode stateCode, List<MatchPlayer> matchPlayers, List<MatchPick> matchPicks) {
        this.matchId = matchId;
        this.round = round;
        this.maxRound = maxRound;
        this.stateCode = stateCode;
        this.matchPlayers = matchPlayers == null ? new ArrayList<>() : matchPlayers;
        this.matchPicks = matchPicks == null ? new ArrayList<>() : matchPicks;
    }

    /**
     * 매치 시작
     */
    private void start() {
        this.stateCode = MatchStateCode.PROCESS;
        this.round = 1;
    }

    /**
     * 매치 종료
     *
     */
    private void end() {
        this.stateCode = MatchStateCode.END;
    }

    /**
     * 라운드 변경
     */
    private void nextRound() {
        // 매치 선택이 완료 되지 않은 경우
        if (Boolean.FALSE.equals(this.isPickedAllMatchPlayers())) {
            throw new NotPickedAllMatchPlayersException();
        }

        // 봇 매치 플레이어 매치 선택 생성
        this.matchPlayers.stream()
                .filter(MatchPlayer::getIsBot)
                .forEach(matchPlayer -> {
                    // 매치 선택 중복 방지
                    if (Boolean.FALSE.equals(this.isPickedMatchPlayerInCurrentRound(matchPlayer.getPlayerId()))) {
                        this.matchPicks.add(MatchPick.generateMatchPick(this, matchPlayer));
                    }
                });

        // 퇴장한 매치 플레이어 매치 선택 생성 (방어 선택 고정)
        this.matchPlayers.stream()
                .filter(matchPlayer -> !matchPlayer.getIsBot() && !matchPlayer.getIsEnter())
                .forEach(matchPlayer -> {
                    // 매치 선택 중복 방지
                    if (Boolean.FALSE.equals(this.isPickedMatchPlayerInCurrentRound(matchPlayer.getPlayerId()))) {
                        this.matchPicks.add(MatchPick.builder()
                                .matchPlayer(matchPlayer)
                                .targetMatchPlayer(matchPlayer)
                                .round(this.round)
                                .pickCode(MatchPickCode.MATCH_PICK_DEFENCE)
                                .pickValue(matchPlayer.getDefence())
                                .build());
                    }
                });

        // 매치 플레이어 선택 적용 (공격, 방어, 회복)
        this.matchPicks.forEach(matchPick -> {
            switch (matchPick.getPickCode()) {
                case MATCH_PICK_DEFENCE -> matchPick.getTargetMatchPlayer().defence();
                case MATCH_PICK_HEAL -> matchPick.getTargetMatchPlayer().heal(matchPick.getPickValue());
                case MATCH_PICK_ATTACK -> matchPick.getTargetMatchPlayer().damage(matchPick.getPickValue());
            }
        });

        // 피해, 회복 수치 적용
        this.matchPlayers.forEach(matchPlayer -> {
            matchPlayer.applyDamageAndRecovery();

            // 매치 플레이어 사망한 경우
            if (Boolean.TRUE.equals(matchPlayer.isDead())) {
                this.end();
            }
        });

        // 마지막 라운드 경우
        if (this.maxRound < this.round + 1) {
            // 매치 종료
            this.end();
        } else {
            // 라운드 수 증가
            this.round = this.round + 1;
        }
    }

    /**
     * 매치 플레이어 입장
     * @param playerId 매치 플레이어 ID
     */
    public void enterMatchPlayer(String playerId) {
        // 이미 매치가 시작한 경우
        if (Boolean.TRUE.equals(this.isStart())) {
            throw new AlreadyStartMatchException();
        }

        // 매치 플레이어 입장 처리
        for (MatchPlayer matchPlayer : matchPlayers) {
            if (matchPlayer.getPlayerId().equals(playerId)) {
                matchPlayer.enter();
                break;
            }
        }

        // 모든 매치 플레이어 입장 완료인 경우 매치 시작
        if (Boolean.TRUE.equals(this.isAllMatchPlayersEntered())) {
            this.start();
        }
    }

    /**
     * 매치 라운드 선택
     * @param matchPick 매치 선택 도메인 객체
     * @return 다음 라운드 진행 여부
     */
    public Boolean pickMatchPlayer(MatchPick matchPick) {

        // 현재 라운드 매치 선택 여부 확인
        if (Boolean.TRUE.equals(this.isPickedMatchPlayerInCurrentRound(matchPick.getMatchPlayer().getPlayerId()))) {
            throw new AlreadyExistsMatchPickException();
        }

        this.matchPicks.add(matchPick);

        // 모든 매치 플레이어 선택 완료인 경우 다음 라운드 진행
        if (Boolean.TRUE.equals(this.isPickedAllMatchPlayers())) {
            this.nextRound();
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * 매치 플레이어 퇴장
     * @param playerId 매치 플레이어 ID
     */
    public void exitMatchPlayer(String playerId) {
        // 매치 플레이어 퇴장 처리
        for (MatchPlayer matchPlayer : matchPlayers) {
            if (matchPlayer.getPlayerId().equals(playerId)) {
                matchPlayer.exit();
                break;
            }
        }

        // 매치 플레이어 1명 or 모든 플레이어 퇴장 상태인 경우
        if (Boolean.TRUE.equals(this.isAllMatchPlayersExited())) {
            // 매치 종료
            this.end();
        }
    }

    /**
     * 승리한 매치 플레이어 조회
     * @return 승리한 매치 플레이어 도메인 객체
     */
    public MatchPlayer getWinner() {
        // 나간 배틀 플레이어
        List<MatchPlayer> rankedMatchPlayers = this.matchPlayers.stream()
                .filter(matchPlayer -> !matchPlayer.getIsEnter())
                .sorted((mp1, mp2) -> {
                    if (mp1.getHp().equals(mp2.getHp())) {
                        return mp1.getExitedAt().compareTo(mp2.getExitedAt());
                    }
                    return mp1.getHp().compareTo(mp2.getHp());
                })
                .collect(Collectors.toList());

        // 나가지 않은 배틀 플레이어
        this.matchPlayers.stream()
                .filter(MatchPlayer::getIsEnter)
                // 역순 정렬
                .sorted((mp1, mp2) -> {
                    if (mp1.getHp().equals(mp2.getHp())) {
                        return mp2.getEnteredAt().compareTo(mp1.getEnteredAt());
                    }
                    return mp1.getHp().compareTo(mp2.getHp());
                })
                // 순위 리스트 앞에서 부터 삽입 (하위 등수 부터 저장)
                .forEachOrdered(matchPlayerEntity -> rankedMatchPlayers.add(0, matchPlayerEntity));

        // 매치 플레이어 없는 경우 예외
        return rankedMatchPlayers.stream().findFirst()
                .orElseThrow(NotExistsMatchPlayerException::new);
    }

    /**
     * 매치 플레이어 조회
     * @param playerId 매치 플레이어 ID
     * @return 매치 플레이어 도메인 객체
     */
    public MatchPlayer getMatchPlayer(String playerId) {
        for (MatchPlayer matchPlayer : matchPlayers) {
            if (matchPlayer.getPlayerId().equals(playerId)) {
                return matchPlayer;
            }
        }

        throw new NotExistsMatchPlayerException();
    }

    /**
     * 매치 시작 여부 확인
     * @return 매치 시작 여부
     */
    public Boolean isStart() {
        return MatchStateCode.PROCESS.equals(this.stateCode);
    }

    /**
     * 매치 종료 여부 확인
     * @return 매치 종료 여부
     */
    public Boolean isEnd() {
        return MatchStateCode.END.equals(this.stateCode);
    }

    /**
     * 마지막 라운드 여부 확인
     * @return 마지막 라운드 여부
     */
    public Boolean isLastRound() {
        return this.maxRound.equals(this.round);
    }

    /**
     * 모든 매치 플레이어 매치 선택 여부 확인
     * @return 매치 선택 여부 확인
     */
    public Boolean isPickedAllMatchPlayers() {
        // 선택한 매치 플레이어 ID 목록
        Set<String> pickedMatchPlayerIds = this.matchPicks.stream()
                .map(matchPick -> matchPick.getMatchPlayer().getPlayerId())
                .collect(Collectors.toSet());

        // 봇이 아닌 매치 플레이어 또는 퇴장한 매치 플레이어 중, 선택 완료인 경우가 아닌 매치 플레이어 존재 여부 확인
        return this.matchPlayers.stream()
                .filter(matchPlayer -> !matchPlayer.getIsBot() && matchPlayer.getIsEnter())
                .filter(matchPlayer -> !pickedMatchPlayerIds.contains(matchPlayer.getPlayerId()))
                .toList()
                .isEmpty();
    }

    /**
     * 현재 라운드 상 매치 선택 완료 여부
     * @param playerId 매치 플레이어 ID
     * @return 매치 선택 완료 여부
     */
    public Boolean isPickedMatchPlayerInCurrentRound(String playerId) {
        for (MatchPick matchPick : this.matchPicks) {
            if (matchPick.getMatchPlayer().getPlayerId().equals(playerId)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * 모든 매치 플레이어 입장 여부 확인
     * @return 모든 매치 플레이어 입장 여부
     */
    public Boolean isAllMatchPlayersEntered() {
        for (MatchPlayer matchPlayer : matchPlayers) {
            if (Boolean.FALSE.equals(matchPlayer.getIsEnter())) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    /**
     * 모든 매치 플레이어 퇴장 or 1명의 매치 플레이어 잔류 여부 확인
     * @return 매치 플레이어 퇴장 여부
     */
    public Boolean isAllMatchPlayersExited() {
        int matchPlayersCount = 0;

        for (MatchPlayer matchPlayer : matchPlayers) {
            if (Boolean.TRUE.equals(matchPlayer.getIsEnter())) {
                matchPlayersCount++;
            }
        }

        return matchPlayersCount <= 1;
    }

    /**
     * 보상 경험치 수치 조회
     * @return 보상 경험치 수치
     */
    public static Double getRewardExp() {
        return REWARD_EXP;
    }

    /**
     * 보상 페이 포인트 조회
     * @return 보상 페이 포인트
     */
    public static Integer getRewardPayPoint() {
        return REWARD_PAY_POINT;
    }

    /**
     * 배팅 페이 포인트 조회
     * @return 배팅 페이 포인트
     */
    public static Integer getBettingPayPoint() {
        return BETTING_PAY_POINT;
    }

    /**
     * 매치 초기 라운드 값 조회
     * @return 초기 라운드 값
     */
    public static Integer getInitRound() {
        return INIT_ROUND;
    }

    /**
     * 매치 초기 상태 값 조회
     * @return 초기 상태 값
     */
    public static MatchStateCode getInitMatchStateCode() {
        return INIT_MATCH_STATE_CODE;
    }

    /**
     * 매치 최대 라운드 수 조회
     * @return 매치 최대 라운드 수
     */
    public static Integer getInitMaxRound() {
        return MAX_ROUND;
    }
}
