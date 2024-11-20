package com.monglife.mongs.app.battle.domain;

import com.monglife.mongs.app.battle.global.enums.BattleRoundCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "battle_round")
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
    public BattleRoundEntity(Long roomId, String playerId, String targetPlayerId, Integer round, BattleRoundCode roundCode, Double roundValue) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.round = round;
        this.roundCode = roundCode;
        this.roundValue = roundValue;
    }
}
