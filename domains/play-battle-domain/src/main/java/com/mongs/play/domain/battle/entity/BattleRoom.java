package com.mongs.play.domain.battle.entity;

import com.mongs.play.module.jpa.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Table(name = "battle_room")
public class BattleRoom extends BaseTimeEntity {
    @Id
    @Builder.Default
    private String roomId = UUID.randomUUID().toString().replaceAll("-", "");
    @Builder.Default
    private Integer round = 0;
    private boolean isActive = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    @Builder.Default
    private Set<BattlePlayer> battlePlayerList = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    @Builder.Default
    private Set<BattleRound> battleRoundList = new HashSet<>();

    public void addBattlePlayer(BattlePlayer battlePlayer) {
        this.battlePlayerList.add(battlePlayer);
    }
    public void removeBattlePlayer(BattlePlayer battlePlayer) {
        this.battlePlayerList.remove(battlePlayer);
    }
    public void addBattleRound(BattleRound battleRound) {
        this.battleRoundList.add(battleRound);
    }
}
