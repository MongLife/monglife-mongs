package com.mongs.play.domain.battle.entity;

import com.mongs.play.domain.battle.code.PickCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "battle_round")
public class BattleRound {
    @Id
    @Builder.Default
    private String roundId = UUID.randomUUID().toString().replace("-", "");
    private String playerId;
    private String targetPlayerId;
    private Integer round;
    @Enumerated(EnumType.STRING)
    private PickCode pick;
}
