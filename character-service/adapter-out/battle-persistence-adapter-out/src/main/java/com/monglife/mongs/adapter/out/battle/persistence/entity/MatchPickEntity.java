package com.monglife.mongs.adapter.out.battle.persistence.entity;

import com.monglife.mongs.domain.battle.enums.MatchPickCode;
import com.monglife.mongs.domain.battle.model.MatchPick;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_match_pick")
@ToString
public class MatchPickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id")
    private Long pickId;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "target_player_id")
    private String targetPlayerId;

    @Column(name = "round_number")
    private Integer round;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_pick_code")
    private MatchPickCode pickCode;

    @Column(name = "pick_value")
    private Double value;

    @Builder
    public MatchPickEntity(Long pickId, String playerId, String targetPlayerId, Integer round, MatchPickCode pickCode, Double value) {
        this.pickId = pickId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.round = round;
        this.pickCode = pickCode;
        this.value = value;
    }

    public void update(MatchPick matchPick) {
        this.playerId = matchPick.getMatchPlayer().getPlayerId();
        this.targetPlayerId = matchPick.getTargetMatchPlayer().getPlayerId();
        this.round = matchPick.getRound();
        this.pickCode = matchPick.getMatchPickCode();
        this.value = matchPick.getValue();
    }

    public MatchPick toDomain(MatchPlayer matchPlayer, MatchPlayer targetMatchPlayer) {
        return MatchPick.builder()
                .pickId(this.pickId)
                .matchPlayer(matchPlayer)
                .targetMatchPlayer(targetMatchPlayer)
                .round(this.round)
                .matchPickCode(this.pickCode)
                .value(this.value)
                .build();
    }
}
