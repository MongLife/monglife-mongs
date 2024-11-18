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
    private String roundId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "target_player_id")
    private Long targetPlayerId;

    @Column(name = "round")
    private Integer round;

    @Enumerated(EnumType.STRING)
    @Column(name = "round_code")
    private BattleRoundCode roundCode;

    @Builder
    public BattleRoundEntity(Long roomId, String playerId, Long targetPlayerId, Integer round, BattleRoundCode roundCode) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.targetPlayerId = targetPlayerId;
        this.round = round;
        this.roundCode = roundCode;
    }
}
