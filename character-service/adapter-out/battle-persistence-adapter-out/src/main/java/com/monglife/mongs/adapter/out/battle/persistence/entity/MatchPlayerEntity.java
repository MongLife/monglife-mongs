package com.monglife.mongs.adapter.out.battle.persistence.entity;

import com.monglife.mongs.domain.battle.model.MatchPlayer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mongs_match_player")
@ToString
public class MatchPlayerEntity {

    @Id
    @Column(name = "player_id")
    private String playerId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "mong_id")
    private Long mongId;

    @Column(name = "mong_code")
    private String mongCode;

    @Column(name = "mong_name")
    private String mongName;

    @Column(name = "name")
    private String name;

    @Column(name = "attack")
    private Double attack;

    @Column(name = "heal")
    private Double heal;

    @Column(name = "defence")
    private Double defence;

    @Column(name = "is_bot")
    private Boolean isBot;

    @Column(name = "hp")
    private Double hp;

    @Column(name = "is_enter")
    private Boolean isEnter;

    @Column(name = "entered_at")
    private LocalDateTime enteredAt;

    @Column(name = "exited_at")
    private LocalDateTime exitedAt;

    @Builder
    public MatchPlayerEntity(String playerId, String deviceId, Long accountId, Long mongId, String mongCode, String mongName, String name, Double attack, Double heal, Double defence, Boolean isBot, Double hp, Boolean isEnter, LocalDateTime enteredAt, LocalDateTime exitedAt) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongCode = mongCode;
        this.mongName = mongName;
        this.name = name;
        this.attack = attack;
        this.heal = heal;
        this.defence = defence;
        this.isBot = isBot;
        this.hp = hp;
        this.isEnter = isEnter;
        this.enteredAt = enteredAt;
        this.exitedAt = exitedAt;
    }

    public void update(MatchPlayer matchPlayer) {
        this.deviceId = matchPlayer.getDeviceId();
        this.accountId = matchPlayer.getAccountId();
        this.mongId = matchPlayer.getMongId();
        this.mongCode = matchPlayer.getMongCode();
        this.mongName = matchPlayer.getMongName();
        this.name = matchPlayer.getName();
        this.attack = matchPlayer.getAttack();
        this.heal = matchPlayer.getHeal();
        this.defence = matchPlayer.getDefence();
        this.isBot = matchPlayer.getIsBot();
        this.hp = matchPlayer.getHp();
        this.isEnter = matchPlayer.getIsEnter();
        this.enteredAt = matchPlayer.getEnteredAt();
        this.exitedAt = matchPlayer.getExitedAt();
    }

    public MatchPlayer toDomain() {
        return MatchPlayer.builder()
                .playerId(this.playerId)
                .deviceId(this.deviceId)
                .accountId(this.accountId)
                .mongId(this.mongId)
                .mongCode(this.mongCode)
                .mongName(this.mongName)
                .name(this.name)
                .attack(this.attack)
                .heal(this.heal)
                .defence(this.defence)
                .isBot(this.isBot)
                .hp(this.hp)
                .isEnter(this.isEnter)
                .enteredAt(this.enteredAt)
                .exitedAt(this.exitedAt)
                .build();
    }
}
