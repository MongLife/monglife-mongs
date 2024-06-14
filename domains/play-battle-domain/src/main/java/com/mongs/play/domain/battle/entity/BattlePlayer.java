package com.mongs.play.domain.battle.entity;

import com.mongs.play.domain.battle.code.PickCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "battle_player")
public class BattlePlayer {
    @Id
    @Builder.Default
    private String playerId = UUID.randomUUID().toString().replaceAll("-", "");
    private Long mongId;
    private String mongCode;

    @Builder.Default
    private PickCode pick = PickCode.NONE;
    @Builder.Default
    private Double hp = 500D;

    private Double attackValue;
    private Double healValue;

    @Builder.Default
    private Boolean isBot = false;

    public void heal() {
        this.hp += healValue;
    }

    public void attacked(Double damage) {
        this.hp -= damage;
    }
}
