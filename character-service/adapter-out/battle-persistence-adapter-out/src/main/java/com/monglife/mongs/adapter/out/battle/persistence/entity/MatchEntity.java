package com.monglife.mongs.adapter.out.battle.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.battle.enums.MatchStateCode;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPick;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_match")
@ToString
public class MatchEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "max_round")
    private Integer maxRound;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_id")
    private List<MatchPlayerEntity> matchPlayers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_id")
    private List<MatchPickEntity> matchPicks = new ArrayList<>();

    @Column(name = "round")
    private Integer round;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_code")
    private MatchStateCode stateCode;

    @Builder
    public MatchEntity(Long matchId, Integer maxRound, List<MatchPlayerEntity> matchPlayers, List<MatchPickEntity> matchPicks, Integer round, MatchStateCode stateCode) {
        this.matchId = matchId;
        this.maxRound = maxRound;
        this.matchPlayers = matchPlayers;
        this.matchPicks = matchPicks;
        this.round = round;
        this.stateCode = stateCode;
    }

    public void update(Match match) {
        match.getMatchPlayers().forEach(matchPlayer -> this.getMatchPlayerEntity(matchPlayer.getPlayerId())
                .ifPresentOrElse(matchPlayerEntity -> matchPlayerEntity.update(matchPlayer), () -> this.matchPlayers.add(MatchPlayerEntity.builder()
                        .playerId(matchPlayer.getPlayerId())
                        .deviceId(matchPlayer.getDeviceId())
                        .accountId(matchPlayer.getAccountId())
                        .mongId(matchPlayer.getMongId())
                        .mongTypeCode(matchPlayer.getMongTypeCode())
                        .mongTypeName(matchPlayer.getMongTypeName())
                        .mongName(matchPlayer.getMongName())
                        .attack(matchPlayer.getAttack())
                        .heal(matchPlayer.getHeal())
                        .defence(matchPlayer.getDefence())
                        .isBot(matchPlayer.getIsBot())
                        .hp(matchPlayer.getHp())
                        .isEnter(matchPlayer.getIsEnter())
                        .enteredAt(matchPlayer.getEnteredAt())
                        .exitedAt(matchPlayer.getExitedAt())
                        .build())));

        match.getMatchPicks().forEach(matchPick -> this.getMatchPickEntity(matchPick.getPickId())
                .ifPresentOrElse(matchPickEntity -> matchPickEntity.update(matchPick), () -> this.matchPicks.add(MatchPickEntity.builder()
                        .playerId(matchPick.getMatchPlayer().getPlayerId())
                        .targetPlayerId(matchPick.getTargetMatchPlayer().getPlayerId())
                        .round(matchPick.getRound())
                        .pickCode(matchPick.getMatchPickCode())
                        .value(matchPick.getValue())
                        .build())));

        this.maxRound = match.getMaxRound();
        this.round = match.getRound();
        this.stateCode = match.getMatchStateCode();
    }

    public Match toDomain() {
        List<MatchPlayer> matchPlayers = this.matchPlayers.stream()
                .map(MatchPlayerEntity::toDomain)
                .collect(Collectors.toList());

        List<MatchPick> matchPicks = this.matchPicks.stream()
                .filter(matchPickEntity -> this.round.equals(matchPickEntity.getRound()))
                .map(matchPickEntity -> {
                    var matchPlayer = matchPlayers.stream()
                            .filter(mp -> mp.getPlayerId().equals(matchPickEntity.getPlayerId()))
                            .findFirst()
                            .orElse(null);

                    var targetMatchPlayer = matchPlayers.stream()
                            .filter(mp -> mp.getPlayerId().equals(matchPickEntity.getTargetPlayerId()))
                            .findFirst()
                            .orElse(null);

                    return matchPickEntity.toDomain(matchPlayer, targetMatchPlayer);

                })
                .collect(Collectors.toList());

        return Match.builder()
                .matchId(this.matchId)
                .round(this.round)
                .maxRound(this.maxRound)
                .matchStateCode(this.stateCode)
                .matchPlayers(matchPlayers)
                .matchPicks(matchPicks)
                .build();
    }

    private Optional<MatchPlayerEntity> getMatchPlayerEntity(String playerId) {
        return this.matchPlayers.stream()
                .filter(mp -> mp.getPlayerId().equals(playerId))
                .findFirst();
    }

    private Optional<MatchPickEntity> getMatchPickEntity(Long pickId) {
        return this.matchPicks.stream()
                .filter(matchPick -> matchPick.getPickId().equals(pickId))
                .findFirst();
    }
}
