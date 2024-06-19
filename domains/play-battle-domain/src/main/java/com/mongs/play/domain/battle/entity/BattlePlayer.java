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
}
