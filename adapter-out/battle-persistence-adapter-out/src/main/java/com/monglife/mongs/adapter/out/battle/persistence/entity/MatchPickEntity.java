package com.monglife.mongs.adapter.out.battle.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_match_pick")
public class MatchPickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id")
    private Long pickId;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "target_player_id")
    private String targetPlayerId;

    @Column(name = "round")
    private Integer round;

    @Column(name = "pick_code")
    private String pickCode;

    @Column(name = "value")
    private Double value;

    @Builder
    public MatchPickEntity(Long pickId, String playerId, String targetPlayerId, Integer round, String pickCode, Double value) {
        this.pickId = pickId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.round = round;
        this.pickCode = pickCode;
        this.value = value;
    }
}
