package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Match {

    // 매치 최대 라운드 수
    private static final int MAX_ROUND = 10;
    // 매치 완료 시 보상 경험치
    private static final double REWARD_EXP = 10D;
    // 매치 승리 시 보상 페이 포인트
    private static final int REWARD_PAY_POINT = 200;
    // 매치 참여 시 배팅 페이 포인트
    private static final int BETTING_PAY_POINT = 50;

    private final Long matchId;

    private Integer round;

    private final Integer maxRound;

    private final List<MatchPlayer> matchPlayers;

    private final MatchRound matchRound;

    @Builder
    public Match(Long matchId, Integer round, List<MatchPlayer> matchPlayers, MatchRound matchRound) {
        this.matchId = matchId;
        this.round = round;
        this.maxRound = MAX_ROUND;
        this.matchPlayers = matchPlayers == null ? new ArrayList<>() : matchPlayers;
        this.matchRound = matchRound;
    }

    /**
     * 매치 최대 라운드 수 조회
     * @return 매치 최대 라운드 수
     */
    public static Integer getMaxRound() {
        return MAX_ROUND;
    }
}
