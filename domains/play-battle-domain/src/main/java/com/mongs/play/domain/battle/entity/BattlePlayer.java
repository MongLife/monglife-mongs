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

    @Builder.Default
    private Boolean isBot = false;

    public void heal() {
        this.hp = Math.min(this.hp + healValue, 500L);
    }

    public void attacked(Double damage) {
        this.hp = Math.max(0, this.hp - damage);
    }

    public void attackedWithHeal(Double damage) {
        this.hp = Math.max(0, this.hp + this.healValue - damage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattlePlayer that = (BattlePlayer) o;
        return Objects.equals(mongId, that.mongId) && Objects.equals(mongCode, that.mongCode) && Objects.equals(hp, that.hp) && Objects.equals(attackValue, that.attackValue) && Objects.equals(healValue, that.healValue) && Objects.equals(isBot, that.isBot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mongId, mongCode, hp, attackValue, healValue, isBot);
    }
}
