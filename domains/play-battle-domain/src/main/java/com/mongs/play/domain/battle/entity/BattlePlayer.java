package com.mongs.play.domain.battle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "battle_player")
public class BattlePlayer {
    @Id
    @Builder.Default
    private String playerId = UUID.randomUUID().toString().replaceAll("-", "");
    private Long mongId;
    private String mongCode;

    private Double hp;
    private Double attackValue;
    private Double healValue;
    private Double defenceValue;

    @Builder.Default
    private Boolean isBot = false;

    public void heal() {
        this.hp = Math.min(this.hp + healValue, 500L);
    }

    public void attacked(Double damage) {
        this.hp = Math.max(0, this.hp - damage);
    }

    public void attackWithDefence(Double damage) {
        Double maxDefenceValue = Math.min(damage, this.defenceValue);
        Double totalDamage = damage - maxDefenceValue;
        this.hp = Math.min(0, this.hp - totalDamage);
    }

    public void attackedWithHeal(Double damage) {
        this.hp = Math.min(Math.max(0, this.hp + this.healValue - damage), 500L);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BattlePlayer battlePlayer) {
            return Objects.equals(this.playerId, battlePlayer.getPlayerId());
        }
        return false;
    }
}
