package com.monglife.mongs.adapter.out.battle.persistence.entity;

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

    @Column(name = "mong_type_code")
    private String mongTypeCode;

    @Column(name = "mong_type_name")
    private String mongTypeName;

    @Column(name = "mong_name")
    private String mongName;

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
    private LocalDateTime enteredDt;

    @Column(name = "exited_at")
    private LocalDateTime exitedAt;

    @Builder
    public MatchPlayerEntity(String playerId, String deviceId, Long accountId, Long mongId, String mongTypeCode, String mongTypeName, String mongName, Double attack, Double heal, Double defence, Boolean isBot, Double hp, Boolean isEnter, LocalDateTime enteredDt, LocalDateTime exitedAt) {
        this.playerId = playerId;
        this.deviceId = deviceId;
        this.accountId = accountId;
        this.mongId = mongId;
        this.mongTypeCode = mongTypeCode;
        this.mongTypeName = mongTypeName;
        this.mongName = mongName;
        this.attack = attack;
        this.heal = heal;
        this.defence = defence;
        this.isBot = isBot;
        this.hp = hp;
        this.isEnter = isEnter;
        this.enteredDt = enteredDt;
        this.exitedAt = exitedAt;
    }
}
