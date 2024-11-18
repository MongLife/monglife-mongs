package com.monglife.mongs.app.battle.domain;

import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "battle_player")
public class BattlePlayerEntity extends BaseTimeEntity {

    @Id
    @Column(name = "player_id")
    private String playerId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "mong_code")
    private String mongCode;

    @Column(name = "hp")
    private Double hp;

    @Column(name = "attack_value")
    private Double attackValue;

    @Column(name = "heal_value")
    private Double healValue;

    @Column(name = "defence_value")
    private Double defenceValue;

    @Column(name = "is_bot")
    private Boolean isBot;

    @Builder
    public BattlePlayerEntity(Long roomId, Long accountId, Long mongId, String mongCode, Double hp, Double attackValue, Double healValue, Double defenceValue, Boolean isBot) {
        this.playerId = UUID.randomUUID().toString().replace("-", "");
        this.roomId = roomId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongCode = mongCode;
        this.hp = hp;
        this.attackValue = attackValue;
        this.healValue = healValue;
        this.defenceValue = defenceValue;
        this.isBot = isBot;
    }

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
}
