package com.monglife.mongs.app.activity.battle.domain;

import com.monglife.mongs.app.activity.battle.enums.BattleRoundCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_activity_battle_round")
public class BattleRoundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_id")
    private Long roundId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "target_player_id")
    private String targetPlayerId;

    @Column(name = "round")
    private Integer round;

    @Enumerated(EnumType.STRING)
    @Column(name = "round_code")
    private BattleRoundCode roundCode;

    @Column(name = "round_value")
    private Double roundValue;


    @Builder
    public BattleRoundEntity(String playerId, String targetPlayerId, Integer round, BattleRoundCode roundCode, Double roundValue) {
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.round = round;
        this.roundCode = roundCode;
        this.roundValue = roundValue;
    }
}
