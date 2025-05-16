package com.monglife.mongs.domain.model;

import com.monglife.mongs.domain.enums.MatchPickCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Random;

@Getter
@ToString
public class MatchPick {

    private static final Random random = new Random();

    private final MatchPlayer matchPlayer;

    private final MatchPlayer targetMatchPlayer;

    private final Integer round;

    private final MatchPickCode matchPickCode;

    private final Double value;

    @Builder
    public MatchPick(MatchPlayer matchPlayer, MatchPlayer targetMatchPlayer, Integer round, MatchPickCode matchPickCode, Double value) {
        this.matchPlayer = matchPlayer;
        this.targetMatchPlayer = targetMatchPlayer;
        this.round = round;
        this.matchPickCode = matchPickCode;
        this.value = value;
    }

    /**
     * 매치 선택 도메인 객체 랜덤 생성
     * @param match 매치 도메인 객체
     * @param matchPlayer 매치 플레이어 도메인 객체
     * @return 매치 선택 도메인 객체
     */
    public static MatchPick generateMatchPick(Match match, MatchPlayer matchPlayer) {

        // 매치 선택 코드 랜덤 선택
        int matchPickIndex = random.nextInt(0, MatchPickCode.values().length);
        MatchPickCode matchPickCode = MatchPickCode.values()[matchPickIndex];

        List<MatchPlayer> targetMatchPlayers = match.getMatchPlayers().stream()
                .filter(targetMatchPlayer -> !targetMatchPlayer.getPlayerId().equals(matchPlayer.getPlayerId()))
                .toList();

        MatchPlayer targetMatchPlayer;
        if (MatchPickCode.MATCH_PICK_ATTACK.equals(matchPickCode)) {
            int targetMatchPlayerIndex = random.nextInt(0, targetMatchPlayers.size());
            targetMatchPlayer = targetMatchPlayers.get(targetMatchPlayerIndex);
        } else {
            targetMatchPlayer = matchPlayer;
        }

        // 매치 선택 코드 조회
        return switch (matchPickCode) {
            case MATCH_PICK_DEFENCE -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(match.getRound())
                    .matchPickCode(matchPickCode)
                    .value(matchPlayer.getDefence())
                    .build();
            case MATCH_PICK_HEAL -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(match.getRound())
                    .matchPickCode(matchPickCode)
                    .value(matchPlayer.getHeal())
                    .build();
            default -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(match.getRound())
                    .matchPickCode(matchPickCode)
                    .value(matchPlayer.getAttack())
                    .build();
        };
    }
}
