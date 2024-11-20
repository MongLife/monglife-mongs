package com.monglife.mongs.app.battle.domain;

import com.monglife.mongs.module.jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "battle_player")
public class BattlePlayerEntity extends BaseTimeEntity {

    @Id
    @Column(name = "player_id")
    private String playerId;

    @Column(name = "device_id")
    private String deviceId;

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
    private Boolean isBot = Boolean.TRUE;

    @Column(name = "is_enter")
    private Boolean isEnter = Boolean.FALSE;

    @Column(name = "enter_dt")
    private LocalDateTime enterDt;

    @Column(name = "exit_dt")
    private LocalDateTime exitDt;

    @Builder
    public BattlePlayerEntity(String playerId, String deviceId, Long roomId, Long accountId, Long mongId, String mongCode, Double hp, Double attackValue, Double healValue, Double defenceValue, Boolean isBot, Boolean isEnter) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.roomId = roomId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongCode = mongCode;
        this.hp = hp;
        this.attackValue = attackValue;
        this.healValue = healValue;
        this.defenceValue = defenceValue;
        this.isBot = isBot;
        this.isEnter = isEnter;
    }

    public void enter() {
        this.isEnter = true;
        this.enterDt = LocalDateTime.now();
    }

    public void exit() {
        this.isEnter = false;
        this.exitDt = LocalDateTime.now();
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
