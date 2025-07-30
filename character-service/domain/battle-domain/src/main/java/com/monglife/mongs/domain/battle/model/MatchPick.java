package com.monglife.mongs.domain.battle.model;

import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Random;

@Getter
@ToString
public class MatchPick {

    private static final Random random = new Random();

    private final Long pickId;

    private final MatchPlayer matchPlayer;

    private final MatchPlayer targetMatchPlayer;

    private final Integer round;

    private final MatchPickCode pickCode;

    private final Double pickValue;

    @Builder
    public MatchPick(Long pickId, MatchPlayer matchPlayer, MatchPlayer targetMatchPlayer, Integer round, MatchPickCode pickCode, Double pickValue) {
        this.pickId = pickId;
        this.matchPlayer = matchPlayer;
        this.targetMatchPlayer = targetMatchPlayer;
        this.round = round;
        this.pickCode = pickCode;
        this.pickValue = pickValue;
    }

    /**
     * 매치 선택 도메인 객체 랜덤 생성
     * @param round 매치 선택 라운드
     * @param matchPlayer 매치 플레이어 도메인 객체
     * @return 매치 선택 도메인 객체
     */
    public static MatchPick generateMatchPick(int round, MatchPlayer matchPlayer, List<MatchPlayer> matchPlayers) {

        // 매치 선택 코드 랜덤 선택
        int matchPickIndex = random.nextInt(0, MatchPickCode.values().length);
        MatchPickCode pickCode = MatchPickCode.values()[matchPickIndex];

        List<MatchPlayer> targetMatchPlayers = matchPlayers.stream()
                .filter(targetMatchPlayer -> !targetMatchPlayer.getPlayerId().equals(matchPlayer.getPlayerId()))
                .toList();

        MatchPlayer targetMatchPlayer;
        if (MatchPickCode.MATCH_PICK_ATTACK.equals(pickCode)) {
            int targetMatchPlayerIndex = random.nextInt(0, targetMatchPlayers.size());
            targetMatchPlayer = targetMatchPlayers.get(targetMatchPlayerIndex);
        } else {
            targetMatchPlayer = matchPlayer;
        }

        // 매치 선택 코드 조회
        return switch (pickCode) {
            case MATCH_PICK_DEFENCE -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(round)
                    .pickCode(pickCode)
                    .pickValue(matchPlayer.getDefence())
                    .build();
            case MATCH_PICK_HEAL -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(round)
                    .pickCode(pickCode)
                    .pickValue(matchPlayer.getHeal())
                    .build();
            default -> MatchPick.builder()
                    .matchPlayer(matchPlayer)
                    .targetMatchPlayer(targetMatchPlayer)
                    .round(round)
                    .pickCode(pickCode)
                    .pickValue(matchPlayer.getAttack())
                    .build();
        };
    }
}
